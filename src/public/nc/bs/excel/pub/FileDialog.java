package nc.bs.excel.pub;


import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;


/**
 * EXCEL �Ի���
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
				if(f.isDirectory()){ //�жϴ��ļ��ǲ��Ǵ˳���·������ʾ���ļ��Ƿ���һ��Ŀ¼ 
			          return true;
			        }
			        return false;
			}
			public String getDescription() {
				// TODO �Զ����ɷ������
				return "�����ļ�";
			}
		});
		jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		return JFileChooser.APPROVE_OPTION == jFileChooser.showOpenDialog(null);//�ж��Ƿ�ȷ�ϵ�ǰ��ѡ��
	}
	
	public String getFilePath(){
		return jFileChooser.getSelectedFile().getPath();
	}
	public String getAbsolutePath(){
		return jFileChooser.getSelectedFile().getParent();
	}

}

