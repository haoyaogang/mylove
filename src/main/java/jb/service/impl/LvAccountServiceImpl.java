package jb.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jb.absx.F;
import jb.dao.LvAccountDaoI;
import jb.dao.LvAccountPhotoDaoI;
import jb.dao.LvFollowDaoI;
import jb.listener.Application;
import jb.model.TlvAccount;
import jb.model.TlvAccountPhoto;
import jb.pageModel.AccountSearch;
import jb.pageModel.DataGrid;
import jb.pageModel.LvAccount;
import jb.pageModel.LvAccountPhoto;
import jb.pageModel.LvVisit;
import jb.pageModel.PageHelper;
import jb.service.LvAccountServiceI;
import jb.service.LvVisitServiceI;
import jb.util.BeanToMapUtil;
import jb.util.Constants;
import jb.util.DateUtil;
import jb.util.MD5Util;
import jb.util.MyBeanUtils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LvAccountServiceImpl extends BaseServiceImpl<LvAccount> implements LvAccountServiceI {

	@Autowired
	private LvAccountDaoI lvAccountDao;
	
	@Autowired
	private LvAccountPhotoDaoI lvAccountPhotoDao;
	
	@Autowired
	private LvFollowDaoI lvFollowDao;
	
	@Autowired
	private LvVisitServiceI lvVisitService;

	@Override
	public DataGrid dataGrid(LvAccount lvAccount, PageHelper ph) {
		List<LvAccount> ol = new ArrayList<LvAccount>();
		String hql = " from TlvAccount t ";
		DataGrid dg = dataGridQuery(hql, ph, lvAccount, lvAccountDao);
		@SuppressWarnings("unchecked")
		List<TlvAccount> l = dg.getRows();
		if (l != null && l.size() > 0) {
			for (TlvAccount t : l) {
				LvAccount o = new LvAccount();
				BeanUtils.copyProperties(t, o);
				ol.add(o);
			}
		}
		dg.setRows(ol);
		return dg;
	}
	

	protected String whereHql(LvAccount lvAccount, Map<String, Object> params) {
		String whereHql = "";	
		if (lvAccount != null) {
			whereHql += " where 1=1 ";
			if (lvAccount.getOpenId() != null) {
				whereHql += " and t.openId = :openId";
				params.put("openId", lvAccount.getOpenId());
			}		
			if (!F.empty(lvAccount.getNickName())) {
				whereHql += " and t.nickName = :nickName";
				params.put("nickName", lvAccount.getNickName());
			}		
			if (!F.empty(lvAccount.getSex())) {
				whereHql += " and t.sex = :sex";
				params.put("sex", lvAccount.getSex());
			}		
			if (!F.empty(lvAccount.getAuditStatus())) {
				whereHql += " and t.auditStatus = :auditStatus";
				params.put("auditStatus", lvAccount.getAuditStatus());
			}		
		}	
		return whereHql;
	}

	@Override
	public void add(LvAccount lvAccount) {
		TlvAccount t = new TlvAccount();
		BeanUtils.copyProperties(lvAccount, t);
		t.setId(UUID.randomUUID().toString());
		//t.setCreatedatetime(new Date());
		lvAccountDao.save(t);
	}

	@Override
	public LvAccount get(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		TlvAccount t = lvAccountDao.get("from TlvAccount t  where t.id = :id", params);
		LvAccount o = new LvAccount();
		BeanUtils.copyProperties(t, o);
		return o;
	}

	@Override
	public void edit(LvAccount lvAccount) {
		TlvAccount t = lvAccountDao.get(TlvAccount.class, lvAccount.getId());
		if (t != null) {
			MyBeanUtils.copyProperties(lvAccount, t, new String[] { "id" , "createTime" },true);
			t.setUpdateTime(new Date());
		}
	}

	@Override
	public void delete(String id) {
		lvAccountDao.delete(lvAccountDao.get(TlvAccount.class, id));
	}

	/**
	 * 注册
	 */
	public LvAccount login(LvAccount lvAccount) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("openId", lvAccount.getOpenId());
		params.put("mobile", lvAccount.getOpenId().toString());
		params.put("password", MD5Util.md5(lvAccount.getPassword()));
		TlvAccount a = lvAccountDao.get("from TlvAccount t where (t.openId = :openId or t.mobile = :mobile) and t.password = :password", params);
		if (a != null) {
			BeanUtils.copyProperties(a, lvAccount);
			return lvAccount;
		}
		return null;
	}
	
	/**
	 * 注册
	 */
	public LvAccount reg(LvAccount account) {
		TlvAccount a = new TlvAccount();
		account.setId(UUID.randomUUID().toString());
		account.setPassword(MD5Util.md5(Constants.ACCOUNT_DEFAULT_PSW));
		account.setHxPassword(Constants.ACCOUNT_DEFAULT_PSW);
		account.setBirthday(DateUtil.getBirthdayByAge(account.getAge()));
		account.setCreateTime(new Date());
		account.setLastLoginTime(new Date());
		MyBeanUtils.copyProperties(account, a, true);
		lvAccountDao.save(a);
		
		return account;
	}


	/**
	 * 修改密码
	 * @throws Exception 
	 */
	public void updatePass(LvAccount lvAccount) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("openId", lvAccount.getOpenId());
		params.put("password", MD5Util.md5(lvAccount.getOldPass()));
		TlvAccount t = lvAccountDao.get("from TlvAccount t where t.openId = :openId and t.password = :password", params);
		if (t == null) {
			throw new Exception("旧密码不正确！");
		} else {
			lvAccount.setPassword(MD5Util.md5(lvAccount.getPassword()));
			MyBeanUtils.copyProperties(lvAccount, t, new String[] {"openId", "createTime"},true);
			t.setUpdateTime(new Date());
		}
	}


	@Override
	public void editByParam(LvAccount lvAccount) throws Exception  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("openId", lvAccount.getOpenId());
		TlvAccount t = lvAccountDao.get("from TlvAccount t where t.openId = :openId", params);
		if (t == null) {
			throw new Exception("用户不存在！");
		} else {
			MyBeanUtils.copyProperties(lvAccount, t, new String[] {"openId", "createTime"},true);
			t.setUpdateTime(new Date());
		}
	}

	/**
	 * 开通VIP
	 */
	public void registerVip(LvAccount lvAccount) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("openId", lvAccount.getOpenId());
		TlvAccount t = lvAccountDao.get("from TlvAccount t where t.openId = :openId", params);
		Date now = DateUtil.parse(DateUtil.format(new Date(), DateUtil.YMD_A), DateUtil.YMD_A);
		if(!F.empty(t.getVipLevel())) {
			if(now.getTime() > t.getVipEndTime().getTime()) {
				lvAccount.setVipOpenTime(now);
				if("VP01".equals(lvAccount.getVipLevel())) {
					lvAccount.setVipEndTime(DateUtil.addYearToDate(now, 1));
				} else if("VP02".equals(lvAccount.getVipLevel())){
					lvAccount.setVipEndTime(DateUtil.addMonthToDate(now, 3));
				} else {
					lvAccount.setVipEndTime(DateUtil.addMonthToDate(now, 1));
				}
			} else {
				if("VP01".equals(t.getVipLevel())) {
					if("VP01".equals(lvAccount.getVipLevel())) {
						lvAccount.setVipEndTime(DateUtil.addYearToDate(t.getVipEndTime(), 1));
					} else if("VP02".equals(lvAccount.getVipLevel())) {
						lvAccount.setVipLevel(t.getVipLevel());
						lvAccount.setVipEndTime(DateUtil.addMonthToDate(t.getVipEndTime(), 3));
					} else {
						lvAccount.setVipLevel(t.getVipLevel());
						lvAccount.setVipEndTime(DateUtil.addMonthToDate(t.getVipEndTime(), 1));
					}
					
				} else if("VP02".equals(t.getVipLevel())){
					if("VP01".equals(lvAccount.getVipLevel())) {
						lvAccount.setVipEndTime(DateUtil.addYearToDate(t.getVipEndTime(), 1));
					} else if("VP02".equals(lvAccount.getVipLevel())) {
						lvAccount.setVipEndTime(DateUtil.addMonthToDate(t.getVipEndTime(), 3));
					} else {
						lvAccount.setVipLevel(t.getVipLevel());
						lvAccount.setVipEndTime(DateUtil.addMonthToDate(t.getVipEndTime(), 1));
					}
				} else {
					if("VP01".equals(lvAccount.getVipLevel())) {
						lvAccount.setVipEndTime(DateUtil.addYearToDate(t.getVipEndTime(), 1));
					} else if("VP02".equals(lvAccount.getVipLevel())) {
						lvAccount.setVipEndTime(DateUtil.addMonthToDate(t.getVipEndTime(), 3));
					} else {
						lvAccount.setVipEndTime(DateUtil.addMonthToDate(t.getVipEndTime(), 1));
					}
				}
			}
		} else {
			lvAccount.setVipOpenTime(now);
			if("VP01".equals(lvAccount.getVipLevel())) {
				lvAccount.setVipEndTime(DateUtil.addYearToDate(now, 1));
			} else if("VP02".equals(lvAccount.getVipLevel())){
				lvAccount.setVipEndTime(DateUtil.addMonthToDate(now, 3));
			} else {
				lvAccount.setVipEndTime(DateUtil.addMonthToDate(now, 1));
			}
		}
		
		MyBeanUtils.copyProperties(lvAccount, t, new String[] {"openId", "createTime"},true);
		t.setUpdateTime(new Date());
	}

	/**
	 * 个人资料查询
	 */
	public LvAccount queryPersonInfoByParam(LvAccount lvAccount) {
		LvAccount a = new LvAccount();
		
		if(lvAccount.getByOpenId() == null) {
			a = this.get(lvAccount.getOpenId());
		} else {
			Map<String, Object> params = new HashMap<String, Object>();
			a = this.get(lvAccount.getByOpenId());
			
			// 获取被查看用户的相册
			List<LvAccountPhoto> photoList = new ArrayList<LvAccountPhoto>();
			params.put("openId", lvAccount.getByOpenId());
			List<TlvAccountPhoto> l = lvAccountPhotoDao.find("from TlvAccountPhoto t where t.openId = :openId order by t.createTime desc", params);
			if(l != null && l.size() > 0) {
				LvAccountPhoto photo = null;
				for(TlvAccountPhoto tPhoto : l) { 
					photo = new LvAccountPhoto();
					MyBeanUtils.copyProperties(tPhoto, photo);
					photoList.add(photo);
				}
			}
			a.setPhotoList(photoList);
			
			// 当前关注状态
			String isVisit = Constants.GLOBAL_BOOLEAN_FALSE; // 未关注
			params = new HashMap<String, Object>();
			params.put("fromOpenId", lvAccount.getOpenId()); 
			params.put("toOpenId", lvAccount.getByOpenId()); 
			if(lvFollowDao.count("select count(*) from TlvFollow t where t.fromOpenId = :fromOpenId and t.toOpenId = :toOpenId", params) > 0) {
				isVisit = Constants.GLOBAL_BOOLEAN_TRUE; // 已关注
			}
			a.setIsVisit(Integer.valueOf(isVisit));
			
			// 插入来访纪录
			LvVisit lvVisit = new LvVisit();
			lvVisit.setOpenId(lvAccount.getByOpenId());
			lvVisit.setVisitOpenId(lvAccount.getOpenId());
			lvVisitService.saveOrUpdate(lvVisit);
			
			// 更改被查看用户的来访数量
			a.setVisitNum(a.getVisitNum() + 1);
			this.edit(a);
		}
		
		a.setAge(DateUtil.getAgeByBirthday(a.getBirthday()));
		return a;
	}


	@Override
	public LvAccount get(Integer openId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("openId", openId);
		TlvAccount t = lvAccountDao.get("from TlvAccount t where t.openId = :openId", params);
		if(t == null) return null;
		LvAccount a = new LvAccount();
		MyBeanUtils.copyProperties(t, a, true);
		
		return a;
	}

	/**
	 * 首页用户查询搜索
	 */
	public DataGrid dataGridAccount_search(AccountSearch search, PageHelper ph) {
		
		DataGrid dg = new DataGrid();
		dg.setPage(Long.valueOf(ph.getPage()));
		dg.setPageSize(Long.valueOf(ph.getRows()));
		
		String hql = "from TlvAccount t  ";
		
		Map<String, Object> params = new HashMap<String, Object>();
		LvAccount a = get(Integer.valueOf(search.getOpenId()));
		String whereHql = " where t.sex = :sex and t.openId != :openId ";
		params.put("sex", "SX01".equals(a.getSex()) ? "SX02" : "SX01"); // 男查女、女查男
		params.put("openId", Integer.valueOf(search.getOpenId())); 
		if("2".equals(search.getSearchType())) {
			if(!F.empty(a.getAddress())) {
				whereHql += " and t.address like :searchAreaCode ";
				params.put("searchAreaCode", "%%" + a.getAddress().split("_")[0] + "%%");
			}
		} else if("3".equals(search.getSearchType())) {
			whereHql += " and t.openId = :searchOpenId ";
			params.put("searchOpenId", Integer.valueOf(search.getSearchOpenId()));
		}
		
		String orderString = " order by t.visitNum desc, t.followNum desc";
		List<TlvAccount> l = lvAccountDao.find(hql + whereHql + orderString, params, ph.getPage(), ph.getRows());
		dg.setTotal(lvAccountDao.count("select count(*) " + hql + whereHql, params));
		
		List<Map<String, Object>> al = new ArrayList<Map<String, Object>>();
		if (l != null && l.size() > 0) {
			Map<String, Object> map = null;
			for (TlvAccount t : l) {
				map = new HashMap<String, Object>();
				LvAccount o = new LvAccount();
				MyBeanUtils.copyProperties(t, o, true);
				o.setAge(DateUtil.getAgeByBirthday(o.getBirthday()));
				map = BeanToMapUtil.convertBean(o, new String[]{"openId", "headImg", "age", "sex", "vipLevel", "vipOpenTime", "vipEndTime", "nickName"});
				al.add(map);
			}
		}
		dg.setRows(al);
		
		return dg;
	}


	/**
	 * 获取一键打招呼列表
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getSayHelloList() {
		String sql = "select t.openId, t.nickName, t.headImg, t.auditStatus from lv_account t where t.openId >= "
				+ "((select max(t1.openId) from lv_account t1)-(select min(t2.openId) FROM lv_account t2)) * RAND()"
				+ " + (select min(t3.openId) from lv_account t3) and t.sex = 'SX02' limit " + Application.getString("SV100");
		List<Map> l = lvAccountDao.findBySql2Map(sql);
		return l == null ? new ArrayList<Map>() : l;
	}


	@Override
	public int getCount(Map<String, Object> params) {
		Long l = lvAccountDao.count("select count(*) from TlvAccount t where t.sex = :sex", params);
		return l == null ? 0 : l.intValue();
	}


	@Override
	public List<LvAccount> findListByHql(String hql,
			Map<String, Object> params, int page, int rows) {
		List<LvAccount> ol = new ArrayList<LvAccount>();
		List<TlvAccount> l = lvAccountDao.find(hql, params, page, rows);
		if (l != null && l.size() > 0) {
			for (TlvAccount t : l) {
				LvAccount o = new LvAccount();
				BeanUtils.copyProperties(t, o);
				ol.add(o);
			}
		}
		return ol;
	}

}
