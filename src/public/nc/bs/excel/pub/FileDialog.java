package nc.bs.excel.pub;


import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;


/**
 * EXCEL 对话框
 */
public class FileDialog {

  JFileChooser jFileChooser = new JFileChooser(); 
	
	public FileDialog() {
		super();
	}
	
	public boolean show(){
		jFileChooser.removeChoosableFileFilter(jFileChooser.getAcceptAllFileFilter());
		jFileChooser.setFileFilter(new FileFilter() {
			public boolean accept(File f) {
				if(f.isDirectory()){ //判断此文件是不是此抽象路径名表示的文件是否是一个目录 
			          return true;
			        }
			        return false;
			}
			public String getDescription() {
				// TODO 自动生成方法存根
				return "所有文件";
			}
		});
		jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		return JFileChooser.APPROVE_OPTION == jFileChooser.showOpenDialog(null);//判断是否确认当前的选择
	}
	
	public String getFilePath(){
		return jFileChooser.getSelectedFile().getPath();
	}
	public String getAbsolutePath(){
		return jFileChooser.getSelectedFile().getParent();
	}

}

