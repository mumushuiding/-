package com.crm.springboot.service.impl;

import static org.mockito.Matchers.anyCollection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.aspectj.weaver.ast.And;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.crm.springboot.mapper.UserMapper;
import com.crm.springboot.pojos.GroupManager;
import com.crm.springboot.pojos.ProcessType;
import com.crm.springboot.pojos.user.Department;
import com.crm.springboot.pojos.user.Dept;
import com.crm.springboot.pojos.user.DeptIdentityLink;
import com.crm.springboot.pojos.user.DeptType;
import com.crm.springboot.pojos.user.Post;
import com.crm.springboot.pojos.user.User;
import com.crm.springboot.pojos.user.UserLinkDept;
import com.crm.springboot.pojos.user.UserLinkPost;
import com.crm.springboot.service.ActivitiService;
import com.crm.springboot.service.SysPowerService;
import com.crm.springboot.service.UserService;
/**
 * User服务接口实现类
 * @Service("userService")用于当前类注释为一个Spring的bean,名为userService
 * @author Administrator
 *
 */
//@CacheConfig(cacheNames="user")
@Service("userService")
public class UserServiceImpl implements UserService{
	
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private ActivitiService activitiService;

    @Autowired SysPowerService sysPowerService;
	@Override
//	@CachePut(key="#t.id")
	public User save(User user) {
		
		userMapper.save(user);
		
		activitiService.saveUser(user.getId());
		//根据职级设置用户组
		
		return user;
	}

	@Override
//	@CacheEvict(key="#id")
	public void deleteById(Serializable id) {
	
		userMapper.deleteById(id);
	}

	@Override
//	@CachePut(key="#t.id")
	
	public User update(User user) {
		
		userMapper.update(user);
		
		return user;
	}

	@Override
//	@Cacheable(key="#id")
	public User getById(String id) {
		
		return userMapper.getById(id);
	}

	@Override
	public List<User> getBySomething(User user) {
		
		return userMapper.getBySomething(user);
	}

	@Override
	public List<User> selectAllUser() {
		
		return userMapper.selectAllUser();
	}

	@Override
	public List<Dept> selectAllDept() {
		
		return userMapper.selectAllDept();
	}

	@Override
	public List<Post> selectAllPost() {
		
		return userMapper.selectAllPost();
	}

	@Override
	public List<UserLinkPost> selectUserLinkPostWithUserId(Serializable userId) {
		
		return userMapper.selectUserLinkPostWithUserId(userId);
	}

	@Override
	public void saveUserLinkPost(HashMap<String, Object> params) {
		
		userMapper.saveUserLinkPostWithUserIdAndPostIds(params);
		
	}

	@Override
	public List<UserLinkDept> selectUserLinkDeptWithUserId(Serializable userId) {
		
		return userMapper.selectUserLinkDeptWithUserId(userId);
	}


	@Override
	public void deleteUsersByUserIds(String[] ids) {
		
		userMapper.deleteUsersByUserIds(ids);
		
	}
	@Override
	public void disableUsersByUserIds(String[] ids) {
		userMapper.disableUsersByUserIds(ids);
		
	}

	@Override
	public String[] getDeptNames(User user) {
		List<UserLinkDept> userLinkDepts=user.getUserLinkDepts();
		if(userLinkDepts==null||userLinkDepts.size()==0) return null;
		List<String> dept=new ArrayList<String>();
		for (int i = 0; i < userLinkDepts.size(); i++) {
			
			
			if(!dept.contains((userLinkDepts.get(i)).getFirstLevel().getName())){
				dept.add((userLinkDepts.get(i)).getFirstLevel().getName());
			}
			
			if(!dept.contains((userLinkDepts.get(i)).getSecondLevel().getName())){
				dept.add((userLinkDepts.get(i)).getSecondLevel().getName());
			}
		}
		String[] deptNames=new String[dept.size()];
		if(dept.size()==0) return null;
		dept.toArray(deptNames);

		return deptNames;
	}

	@Override
	public void saveUserLinkDeptWithUserLinkDeptAndFirstLevelIds(HashMap<String, Object> params) {
		userMapper.saveUserLinkDeptWithUserLinkDeptAndFirstLevelIds(params);
		
	}

	@Override
	public void saveDept(Dept dept) {
		userMapper.saveDept(dept);
		
	}

	@Override
	public void saveDeptIdentityLink(DeptIdentityLink deptIdentityLink) {
		userMapper.saveDeptIdentityLink(deptIdentityLink);
		
	}
	@Override
	public void saveUserLinkDept(UserLinkDept userLinkDept) {
		
		this.saveUserLinkDept(userLinkDept.getUserId(), userLinkDept.getFirstLevelIds(), String.valueOf(userLinkDept.getSecondLevel().getDid()), String.valueOf(userLinkDept.getThirdLevel().getDid()));
	}


	@Override
	public void saveUserLinkDept(Integer userId, String firstLevelId, String secondLevelId, String thirdLevelId) {
		UserLinkDept userLinkDept=new UserLinkDept();
		userLinkDept.setUserId(userId);

		//第二列为secondLevel，次级部门
		if(!"".equals(secondLevelId)){
			Dept secondLevel=new Dept();
			secondLevel.setDid(Integer.valueOf(secondLevelId));
			userLinkDept.setSecondLevel(secondLevel);
		}
		
		//第三列为thirdLevel，第三级部门
		if(!"".equals(thirdLevelId)){
			Dept thirdLevel=new Dept();
            thirdLevel.setDid(Integer.valueOf(thirdLevelId));
			userLinkDept.setThirdLevel(thirdLevel);
		}
	    if(firstLevelId!=null&&!"".equals(firstLevelId)){
	    	
	    	HashMap<String, Object> params=new HashMap<String, Object>();
	    	//第一列为firstLevel，最底层部门
	    	String[] firstLevelIds=firstLevelId.split(",");

			params.put("firstLevelIds", firstLevelIds);
			params.put("userLinkDept", userLinkDept);
			userMapper.saveUserLinkDeptWithUserLinkDeptAndFirstLevelIds(params);
	    }else{
	    	userMapper.saveUserLinkDeptWithUserLinkDept(userLinkDept);
	    }
		
				
	}

	@Override
	public List<User> selectAllUserWithHashMap(HashMap<String, Object> params) {
		
		return userMapper.selectAllUserWithHashMap(params);
	}

	@Override
	public List<Dept> selectDistinctSecondLevelDept() {
		
		return userMapper.selectDistinctSecondLevelDept();
	}

	@Override
	public List<Dept> selectDistinctFirstLevelDept(HashMap<String, Object> params){
		
		return userMapper.selectDistinctFirstLevelDept(params);
	}

	@Override
	public List<UserLinkDept> selectUserLinkDeptWithResutltType(HashMap<String, Object> params) {
		
		return userMapper.selectUserLinkDeptWithResutltType(params);
	}

	@Override
	public void deleteUserLinkDept(HashMap<String, Object> params) {
		userMapper.deleteUserLinkDept(params);
		
	}

	@Override
	public List<String> selectAllUserLinkDeptIds(HashMap<String, Object> params) {
		
		return userMapper.selectAllUserLinkDeptIds(params);
	}

	@Override
	public void deleteUserLinkDeptByIds(String[] ids) {
		userMapper.deleteUserLinkDeptByIds(ids);
		
	}

	@Override
	public UserLinkDept getSingleUserLinkDept(User user, String deptName) {
		
		UserLinkDept result=null;
		List<UserLinkDept> userLinkDepts=user.getUserLinkDepts();
		
		
		if(userLinkDepts==null && userLinkDepts.size()==0) return null;
		
		if(userLinkDepts.size()==1){
			if(deptName==null) return userLinkDepts.get(0);
			
		}
		if(deptName==null) return null;
		
		for (UserLinkDept userLinkDept : userLinkDepts) {
			if(userLinkDept.getFirstLevel().getName().equals(deptName)){
				result=userLinkDept;break;
			}
		}
		if(result==null){
			for (UserLinkDept userLinkDept : userLinkDepts) {
				if(userLinkDept.getSecondLevel().getName().equals(deptName)){
					result=userLinkDept;break;
				}
			}
		}
		
        
		return result;
	}

	@Override
	public String selectUserIdByPhone(String phone) {
		
		return userMapper.selectUserIdByPhone(phone);
	}

	@Override
	public void saveAndUpdateUserLinkDeptWithUser(User user) {
		UserLinkDept userLinkDept=user.getUserLinkDept();

		if(userLinkDept==null||userLinkDept.getFirstLevelIds()==null||userLinkDept.getFirstLevelIds()=="") return;
		HashMap<String, Object> params=new HashMap<String, Object>();
		
		params.put("userId", user.getId());
		List<UserLinkDept> userLinkDepts=this.selectUserLinkDeptWithResutltType(params);
		
		params.remove("userId");
		
		//先删除全部旧有数据
		if(userLinkDepts!=null&&userLinkDepts.size()>0){
			for (UserLinkDept userLinkDept2 : userLinkDepts) {
				params.put("id", userLinkDept2.getId());
				this.deleteUserLinkDept(params);
			}
		}
		params.remove("id");
		//添加新数据
		userLinkDept.setUserId(user.getId());
		params.put("firstLevelIds",userLinkDept.getFirstLevelIds().split(","));
		params.put("userLinkDept", userLinkDept);
		this.saveUserLinkDeptWithUserLinkDeptAndFirstLevelIds(params);
		
	}

	@Override
	public void saveGroupManagerWithUser(User user) {
		//设置一个普通员工组给任一员工
		
		sysPowerService.saveGroupToUser(user, null, new String[30]);
		if(user.getPost()==null||user.getPost().getName()==null||user.getPost().getName()=="") return;
			
	}

	@Override
	public Dept selectSingleDept(HashMap<String, Object> params) {
		List<Dept> depts=this.selectAllDepts(params);
		if(depts==null || depts.size()==0) return null;
		return depts.get(0);
	}

	@Override
	public List<Dept> selectAllDepts(HashMap<String, Object> params) {
		
		return userMapper.selectAllDepts(params);
	}

	@Override
	public HashMap<String, Object> selectSingleDeptAsHashMap(HashMap<String, Object> params) {
		List<HashMap<String, Object>> dMaps=this.selectAllDeptsAsHashMap(params);
		if(dMaps==null&&dMaps.size()==0)return null;
		return dMaps.get(0);
	}

	@Override
	public List<HashMap<String, Object>> selectAllDeptsAsHashMap(HashMap<String, Object> params) {
		
		return userMapper.selectAllDeptsAsHashMap(params);
	}

	@Override
	public User selectSingleUserWithHashMap(HashMap<String, Object> params) {
		List<User> users=selectAllUserWithHashMap(params);
		if(users==null||users.size()==0) return null;
		return users.get(0);
	}

	@Override
	public User selectUserByUserIdAsType(String id) {
	
		return userMapper.selectUserByUserIdAsType(id);
	}

	@Override
	public List<DeptType> selectAllDeptType(HashMap<String, Object> params) {
		
		return userMapper.selectAllDeptType(params);
	}

	@Override
	public List<DeptType> selectAllDeptType(int deptId, int dicId) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("deptId", deptId);
		params.put("dicId", dicId);
		List<DeptType> result=this.selectAllDeptType(params);
		if(result==null||result.size()==0) return null;
		return result;
	}

	@Override
	public void saveDeptType(DeptType deptType) {
		userMapper.saveDeptType(deptType);
		
	}

	@Override
	public Dept selectSingleDept(int deptId) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		
		params.put("deptId", String.valueOf(deptId));
		
		return this.selectSingleDept(params);
	}

	@Override
	public Dept selectSingleDept(String deptName) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("name", deptName);
		return this.selectSingleDept(params);
	}

	@Override
	public DeptType selectSingleDeptType(int deptId) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		
		params.put("deptId", String.valueOf(deptId));
		List<DeptType> deptTypes=this.selectAllDeptType(params);
		if(deptTypes==null || deptTypes.size()==0) return null;
		return deptTypes.get(0);
	}

	@Override
	public DeptType selectSingleDeptType(String deptName) {
		Dept dept=this.selectSingleDept(deptName);
		if(dept==null)return null;
		return this.selectSingleDeptType(dept.getDid());
	}

	@Override
	public Integer selectCountUserNumber(HashMap<String, Object> params) {
		
		return userMapper.selectCountUserNumber(params);
	}
	@Override
	public Integer selectCountUserNumber(Integer deptId, String deptLevel, String postIds) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("postIds", postIds.split(","));
		params.put("deptLevel", deptLevel);
		params.put("deptId", deptId);
		return this.selectCountUserNumber(params);
	}

	@Override
	public Integer selectCountUserNumberWithFirsDeptId(Integer firstDeptId, String postIds) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("firstLevelId", firstDeptId);
		params.put("postIds", postIds.split(","));
		
		return this.selectCountUserNumber(params);
	}

	@Override
	public Integer selectCountUserNumberWithSecondDeptId(Integer secondDeptId, String postIds) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("secondLevelId", secondDeptId);
		params.put("postIds", postIds.split(","));
		
		return this.selectCountUserNumber(params);
	}

	@Override
	public List<String> selectDistinctFirstDeptNames(HashMap<String, Object> params) {
		
		return userMapper.selectDistinctFirstDeptNames(params);
	}

	@Override
	public List<String> selectDistinctFirstDeptNames(Integer secondDeptId) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("secondLevelId", secondDeptId);
		return this.selectDistinctFirstDeptNames(params);
	}

	@Override
	public List<User> selectUserWithDeptNameAndGroupName(String deptName, String groupName) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("deptName", deptName);
		params.put("groupName", groupName);
		
		return userMapper.selectUserWithDeptNameAndGroupName(params);
	}

	@Override
	public List<String> getDeptNames(String deptName, String deptLevel) {
		Dept dept=this.selectSingleDept(deptName);
		if(dept==null) throw new RuntimeException("该部门["+deptName+"]不存在");
		    
		List<String> dnames=new ArrayList<String>();//涉及到的部门
		
		if(ProcessType.DEPT_LEVEL_SECOND.equals(deptLevel)){
			
			dnames=this.selectDistinctFirstDeptNames(dept.getDid());
		}
        if (ProcessType.DEPT_LEVEL_FIRST.equals(deptLevel)) {
        	dnames.add(deptName);
		}
		
		return dnames;
	}

	@Override
	public List<String> selectUserIdWithDeptNameAndGroupName(HashMap<String, Object> params) {
		List<String> result=userMapper.selectUserIdWithDeptNameAndGroupName(params);
		if(result==null||result.size()==0) return null;
		return result;
	}

	@Override
	public List<String> selectUserIdWithDeptNameAndGroupName(List<String> deptNames, String groupName) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("deptName", deptNames);
		params.put("groupName", groupName.split(","));
		return this.selectUserIdWithDeptNameAndGroupName(params);
	}

	@Override
	public List<String> selectSingleColumnFromInfoDept(HashMap<String, Object> params) {
		
		return userMapper.selectSingleColumnFromInfoDept(params);
	}

	@Override
	public List<String> selectSingleColumnFromInfoDept(String column, String level, String deptName) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("column", column);
		if(!"".equals(level)) params.put("level", level);//部门级别
		if(!"".equals(deptName)) params.put("deptName", deptName);
		return this.selectSingleColumnFromInfoDept(params);
	}

	@Override
	public List<User> selectUserAsResultType(HashMap<String, Object> params) {
		
		return userMapper.selectUserAsResultType(params);
	}

	@Override
	public User selectSingleUserAsResultType(HashMap<String, Object> params) {
		List<User> users=this.selectUserAsResultType(params);
		if(users==null || users.size()==0) return null;
		return users.get(0);
	}

	@Override
	public User selectSingleUserAsResultType(String phone, String email) {
		HashMap<String,Object> params=new HashMap<String,Object>();
		params.put("phone", phone);
		params.put("email", email);
		return this.selectSingleUserAsResultType(params);
	}

	@Override
	public boolean isContainsGroupLike(User user, String groupnameLike) {
		for (GroupManager groupManager : user.getUserLinkGroup()) {
			if (groupManager.getGroupTable().getGroupname().contains(groupnameLike)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<UserLinkDept> selectAllUserLinkDept(HashMap<String, Object> params) {
		
		return userMapper.selectAllUserLinkDept(params);
	}

	@Override
	public UserLinkDept selectSingleUserLinkDept(HashMap<String, Object> params) {
		List<UserLinkDept> userLinkDepts=this.selectAllUserLinkDept(params);
		if(userLinkDepts==null||userLinkDepts.size()==0) return null;
		return userLinkDepts.get(0);
	}

	@Override
	public UserLinkDept selectSingleUserLinkDept(String firstDeptName) {
		HashMap<String, Object> parHashMap=new HashMap<String, Object>();
		parHashMap.put("firstDeptName", firstDeptName);
		return this.selectSingleUserLinkDept(parHashMap);
	}

	@Override
	public List<User> selectUser(HashMap<String, Object> params) {
		
		return userMapper.selectUser(params);
	}

	@Override
	public User selectSingleUser(HashMap<String, Object> params) {
		List<User> users=this.selectUser(params);
		if(users==null||users.size()==0) return null;
		return users.get(0);
	}

	@Override
	public User selectSingleUserWithPhone(String phone) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		//params.put("columns", "id,username,phone,postId");
		params.put("whereClause", "phone="+phone);
		return this.selectSingleUser(params);
	}

	@Override
	public boolean isDeptExists(Department dept) {
		List<Dept> depts=this.selectDeptWithDepartment(dept);
		if(depts==null||depts.size()==0) return false;
		return true;
	}

	@Override
	public List<Dept> selectDeptWithDepartment(Department dept) {
		
		return userMapper.selectDeptWithDepartment(dept);
	}

	@Override
	public void saveDepartment(Department department) {
		userMapper.saveDepartment(department);
		
	}

	@Override
	public void saveDeptIdentityLinkWithHashMap(HashMap<String, Object> params) {
		userMapper.saveDeptIdentityLinkWithHashMap(params);
		
	}

	@Override
	public void saveDeptIdentityLink(String firstId, String secondId, String thirdId) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("firstId", firstId);
		params.put("secondId", secondId);
		params.put("thirdId", thirdId);
		this.saveDeptIdentityLinkWithHashMap(params);
		
	}

	@Override
	public String getDeptName(User user) {
		List<UserLinkDept> depts = user.getUserLinkDepts();
		if(!CollectionUtils.isEmpty(depts)) {
			if(depts.size()==1) {
				 return depts.get(0).getFirstLevel().getName();
			}else {
				return depts.get(0).getSecondLevel().getName();
			}
		}
		return "";
	}



















}
