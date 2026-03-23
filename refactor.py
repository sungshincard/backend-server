import os
import re

domain_dir = r'C:\workspace\card-shopping-mall-project\backend-server\src\main\java\com\sungshincard\backend\domain'
base_entity_import = 'import com.sungshincard.backend.common.entity.BaseTimeEntity;'

for root, dirs, files in os.walk(domain_dir):
    for file in files:
        if file.endswith('.java'):
            filepath = os.path.join(root, file)
            with open(filepath, 'r', encoding='utf-8') as f:
                content = f.read()

            if '@CreatedDate' in content or '@LastModifiedDate' in content:
                # Add import if missing
                if base_entity_import not in content:
                    content = re.sub(r'(package .*?;)', f'\\1\n\n{base_entity_import}', content, count=1)
                
                # Remove specific imports
                content = re.sub(r'import org\.springframework\.data\.annotation\.CreatedDate;\n?', '', content)
                content = re.sub(r'import org\.springframework\.data\.annotation\.LastModifiedDate;\n?', '', content)
                
                # Remove createdAt block
                content = re.sub(r'\s*@CreatedDate\s*@Column\([^)]*\)\s*private LocalDateTime createdAt;', '', content)
                
                # Remove updatedAt block
                content = re.sub(r'\s*@LastModifiedDate\s*@Column\([^)]*\)\s*private LocalDateTime updatedAt;', '', content)

                # Add extends BaseTimeEntity to class declaration
                # We need to find `public class X {` or `public class X implements Y {`
                # Let's match: public class MyClass
                # Also, we need to handle if it already has 'extends' (unlikely for these but good to be safe, actually none of these seem to extend anything right now based on our read).
                
                class_decl_pattern = r'(public\s+class\s+\w+)(?:\s+implements\s+[^{]+)?\s*\{'
                
                def replace_class_decl(match):
                    decl = match.group(0)
                    if 'extends ' not in decl:
                        return decl.replace('class ', 'class ', 1).replace(match.group(1), match.group(1) + ' extends BaseTimeEntity')
                    return decl
                
                content = re.sub(class_decl_pattern, replace_class_decl, content, count=1)
                
                with open(filepath, 'w', encoding='utf-8') as f:
                    f.write(content)
                
                print(f"Refactored: {filepath}")
