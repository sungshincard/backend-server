const fs = require('fs');
const path = require('path');

const domainDir = path.join('C:', 'workspace', 'card-shopping-mall-project', 'backend-server', 'src', 'main', 'java', 'com', 'sungshincard', 'backend', 'domain');
const baseEntityImport = 'import com.sungshincard.backend.common.entity.BaseTimeEntity;';

function processDir(dir) {
    const files = fs.readdirSync(dir);
    for (const file of files) {
        const filePath = path.join(dir, file);
        if (fs.statSync(filePath).isDirectory()) {
            processDir(filePath);
        } else if (file.endsWith('.java')) {
            let content = fs.readFileSync(filePath, 'utf8');
            if (content.includes('@CreatedDate') || content.includes('@LastModifiedDate')) {
                let originalContent = content;

                // 1. Add import
                if (!content.includes(baseEntityImport)) {
                    content = content.replace(/(package .*?;)/, `$1\n\n${baseEntityImport}`);
                }

                // 2. Remove old imports
                content = content.replace(/import org\.springframework\.data\.annotation\.CreatedDate;\r?\n?/g, '');
                content = content.replace(/import org\.springframework\.data\.annotation\.LastModifiedDate;\r?\n?/g, '');

                // 3. Remove createdAt block
                content = content.replace(/\s*@CreatedDate[\s\S]*?private LocalDateTime createdAt;/g, '');

                // 4. Remove updatedAt block
                content = content.replace(/\s*@LastModifiedDate[\s\S]*?private LocalDateTime updatedAt;/g, '');

                // 5. Add extends BaseTimeEntity
                const classDeclRegex = /(public\s+class\s+\w+)(?:\s+implements\s+[^{]+)?\s*\{/;
                const match = classDeclRegex.exec(content);
                if (match && !match[0].includes('extends BaseTimeEntity')) {
                    if (!match[0].includes('extends ')) {
                        const newDecl = match[0].replace(match[1], match[1] + ' extends BaseTimeEntity');
                        content = content.replace(match[0], newDecl);
                    }
                }

                if (content !== originalContent) {
                    fs.writeFileSync(filePath, content, 'utf8');
                    console.log('Refactored: ' + filePath);
                }
            }
        }
    }
}

processDir(domainDir);
