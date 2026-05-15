import os

def collect_source_code():
    project_root = os.getcwd()
    output_file = "all_source_code.txt"
    
    # Atlanacak klasörler
    exclude_dirs = {'.git', '.gradle', '.idea', 'build', 'target', 'node_modules', '.gemini', 'bin', 'obj'}
    # Dahil edilecek uzantılar
    include_extensions = {'.java', '.kt', '.xml', '.properties', '.yml', '.js', 'Dockerfile'}
    
    with open(output_file, 'w', encoding='utf-8') as outfile:
        for root, dirs, files in os.walk(project_root):
            # Exclude directories in-place
            dirs[:] = [d for d in dirs if d not in exclude_dirs]
            
            for file in files:
                if any(file.endswith(ext) for ext in include_extensions) or file == 'Dockerfile':
                    full_path = os.path.join(root, file)
                    rel_path = os.path.relpath(full_path, project_root)
                    
                    outfile.write("=" * 80 + "\n")
                    outfile.write(f"FILE: {rel_path}\n")
                    outfile.write("=" * 80 + "\n")
                    
                    try:
                        with open(full_path, 'r', encoding='utf-8', errors='ignore') as infile:
                            outfile.write(infile.read())
                    except Exception as e:
                        outfile.write(f"Error reading file: {e}\n")
                    
                    outfile.write("\n\n")

    print(f"Bütün kodlar {output_file} dosyasına başarıyla kaydedildi.")

if __name__ == "__main__":
    collect_source_code()
