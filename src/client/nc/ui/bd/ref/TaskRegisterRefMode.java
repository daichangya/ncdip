package nc.ui.bd.ref;
/**
 * @author wyd
 * @desc 预警任务类型参照
 *
 * */
 //<nc.ui.bd.ref.TaskRegisterRefMode>
public class TaskRegisterRefMode extends AbstractRefModel {
		//设置不显示字段：如 主键
		public String[]getHiddenFieldCode(){
			return new String[]{"dip_taskregister.pk_taskregister"};
		}
		public TaskRegisterRefMode(){
			super();
		}
		//设置select子句，显示编码、类型字段
		public String[] getFieldCode(){
			return new String[]{"dip_taskregister.code","dip_taskregister.name"};
		}
		//显示中文名称：表头和栏目用
		public String[] getFieldName(){
			return new String[]{
					"任务类型编码","任务类型名称"	
			};
			}
			//参照窗体标题
			public String getRefTitle(){
				return "任务类型";
			}

			//表名：设置form子句
			public String getTableName(){
				return "dip_taskregister";
			}

		
		//设定主键字段，必须在getHiddenFieldCode或setFieldCode已设定
		@Override
		public String getPkFieldCode() {
			return " dip_taskregister.pk_taskregister" ;
		}
		/**
		 * 标准名称查找指定档案：数据来源类型
		 * 设定where子句
		 */
		public  String getWherePart(){
			
			return (super.getWherePart()==null||super.getWherePart().length()<=0?" ":" and ")+" pk_taskregister <> '0001AA1000000001FVAD'  and nvl(dr,0)=0 ";
		}
		
		//2011-7-5 
		@Override
		public void addWherePart(String newWherePart) {
			super.addWherePart(newWherePart);
		}

			
		
		

	}
