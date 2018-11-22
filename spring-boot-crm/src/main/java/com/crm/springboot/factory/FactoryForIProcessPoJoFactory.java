package com.crm.springboot.factory;

import com.crm.springboot.pojos.ProcessType;


public  class  FactoryForIProcessPoJoFactory implements IFactoryForIProcessPojoFactory{
    private FactoryForIProcessPoJoFactory(){};
    //单例模式，直接生成一个对象
    public static FactoryForIProcessPoJoFactory factoryForIProcessPoJoFactory=new FactoryForIProcessPoJoFactory();
	@Override
	public AProcessPojoFactory getProcessPojoFactory(String buinessType) {
		AProcessPojoFactory factory=null;
		switch (buinessType) {
		case ProcessType.BUSINESSTYPE_GROUPMANAGER:
			factory=new GroupManagerFactoryForProcess();
			break;
		case ProcessType.BUSINESSTYPE_DELETEUSER:
			factory=new DeleteUserFactoryForProcess();
			break;
		case ProcessType.BUSINESSTYPE_DISABLEUSER:
			factory=new DisableUserFactoryForProcess();
			break;
		case ProcessType.BUSINESSTYPE_DELETEMARKSBYOVERTIME:
			factory=new DeleteMarksByOverTimeFactoryForProcess();
			break;
		case ProcessType.BUSINESSTYPE_ADDMARKS:
			factory=new AddMarksFactoryForProcess();
			break;
		case ProcessType.BUSINESSTYPE_UPDATEMARK:
			factory=new UpdateMarkFactoryForProcess();
			break;	
		case ProcessType.BUSINESSTYPE_DELETEMARK:
			factory=new DeleteMarkFactoryForProcess();
			break;
		
		case ProcessType.BUSINESSTYPE_ADDDEPARTMENT:
			factory=new AddDepartmentFacotoryForProcess();
			break;
			
		default:
			break;
		}
		return factory;
	}
   public static FactoryForIProcessPoJoFactory getInstance(){

	  return factoryForIProcessPoJoFactory;
	   
   }
}
