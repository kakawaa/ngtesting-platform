package com.ngtesting.platform.action.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ngtesting.platform.action.BaseAction;
import com.ngtesting.platform.config.Constant;
import com.ngtesting.platform.model.IsuPage;
import com.ngtesting.platform.model.IsuPageSolution;
import com.ngtesting.platform.model.TstUser;
import com.ngtesting.platform.service.IssuePageService;
import com.ngtesting.platform.service.IssuePageSolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(Constant.API_PATH_ADMIN + "issue_page_solution/")
public class IssuePageSolutionAdmin extends BaseAction {
    @Autowired
    IssuePageService pageService;
	@Autowired
    IssuePageSolutionService pageSolutionService;

	@RequestMapping(value = "load", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> load(HttpServletRequest request, @RequestBody JSONObject json) {
		Map<String, Object> ret = new HashMap<String, Object>();

		TstUser userVo = (TstUser) request.getSession().getAttribute(Constant.HTTP_SESSION_USER_PROFILE);
		Integer orgId = userVo.getDefaultOrgId();

		List<IsuPageSolution> vos = pageSolutionService.list(orgId);

		ret.put("code", Constant.RespCode.SUCCESS.getCode());
		ret.put("solutions", vos);
		return ret;
	}

	@RequestMapping(value = "get", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> get(HttpServletRequest request, @RequestBody JSONObject json) {
		Map<String, Object> ret = new HashMap<String, Object>();

		TstUser userVo = (TstUser) request.getSession().getAttribute(Constant.HTTP_SESSION_USER_PROFILE);
		Integer orgId = userVo.getDefaultOrgId();

		Integer solutionId = json.getInteger("id");
		IsuPageSolution solution = null;
		if (solutionId == null) {
			solution = new IsuPageSolution();
		} else {
			solution = pageSolutionService.get(solutionId, orgId);
		}

		ret.put("solution", solution);
		ret.put("code", Constant.RespCode.SUCCESS.getCode());
		return ret;
	}

	@RequestMapping(value = "getConfig", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getConfig(HttpServletRequest request, @RequestBody JSONObject json) {
		Map<String, Object> ret = new HashMap<String, Object>();

		TstUser userVo = (TstUser) request.getSession().getAttribute(Constant.HTTP_SESSION_USER_PROFILE);
		Integer orgId = userVo.getDefaultOrgId();

		Integer solutionId = json.getInteger("id");

        IsuPageSolution solution = pageSolutionService.get(solutionId, orgId);
        Map itemMap = pageSolutionService.getItemsMap(solutionId, orgId);

        List<IsuPage> pages = pageService.list(orgId);

		ret.put("solution", solution);
        ret.put("itemMap", itemMap);
        ret.put("pages", pages);
		ret.put("code", Constant.RespCode.SUCCESS.getCode());
		return ret;
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> save(HttpServletRequest request, @RequestBody JSONObject json) {
		Map<String, Object> ret = new HashMap<String, Object>();

		TstUser userVo = (TstUser) request.getSession().getAttribute(Constant.HTTP_SESSION_USER_PROFILE);
		Integer orgId = userVo.getDefaultOrgId();

		IsuPageSolution vo = JSON.parseObject(JSON.toJSONString(json), IsuPageSolution.class);
		pageSolutionService.save(vo, orgId);

		ret.put("solution", vo);

		ret.put("code", Constant.RespCode.SUCCESS.getCode());
		return ret;
	}

	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delete(HttpServletRequest request, @RequestBody JSONObject json) {
		Map<String, Object> ret = new HashMap<String, Object>();

		TstUser userVo = (TstUser) request.getSession().getAttribute(Constant.HTTP_SESSION_USER_PROFILE);
		Integer orgId = userVo.getDefaultOrgId();

		Integer id = json.getInteger("id");

		boolean success = pageSolutionService.delete(id, orgId);

		ret.put("code", Constant.RespCode.SUCCESS.getCode());
		return ret;
	}

}