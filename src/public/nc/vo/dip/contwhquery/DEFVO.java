package nc.vo.dip.contwhquery;

import nc.vo.pub.SuperVO;

public 	class DEFVO extends SuperVO{
		private String pk;
		private String npk;
		private String wpk;
		
		
		public String getNpk() {
			return npk;
		}
		public void setNpk(String npk) {
			this.npk = npk;
		}
		public String getPk() {
			return pk;
		}
		public void setPk(String pk) {
			this.pk = pk;
		}
		public String getWpk() {
			return wpk;
		}
		public void setWpk(String wpk) {
			this.wpk = wpk;
		}
		@Override
		public String getPKFieldName() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public String getParentPKFieldName() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public String getTableName() {
			// TODO Auto-generated method stub
			return null;
		}
	}