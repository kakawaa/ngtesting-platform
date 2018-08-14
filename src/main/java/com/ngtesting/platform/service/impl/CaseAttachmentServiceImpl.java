package com.ngtesting.platform.service.impl;

import com.ngtesting.platform.config.Constant;
import com.ngtesting.platform.dao.CaseAttachmentDao;
import com.ngtesting.platform.dao.CaseDao;
import com.ngtesting.platform.model.TstCase;
import com.ngtesting.platform.model.TstCaseAttachment;
import com.ngtesting.platform.model.TstUser;
import com.ngtesting.platform.service.CaseAttachmentService;
import com.ngtesting.platform.service.CaseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CaseAttachmentServiceImpl extends BaseServiceImpl implements CaseAttachmentService {
    @Autowired
    CaseAttachmentDao caseAttachmentDao;
    @Autowired
    CaseHistoryService caseHistoryService;
    @Autowired
    CaseDao caseDao;

    @Override
    public Boolean save(Integer caseId, String name, String path, TstUser user) {
        TstCase testCase = caseDao.get(caseId, user.getDefaultPrjId());
        if (testCase == null) {
            return false;
        }

        TstCaseAttachment attach = new TstCaseAttachment(name, path, caseId, user.getId());
        caseAttachmentDao.save(attach);
        caseHistoryService.saveHistory(user, Constant.CaseAct.upload_attachment, testCase, name);
        return true;
    }

    @Override
    public Boolean delete(Integer id, TstUser user) {
        TstCaseAttachment attach = caseAttachmentDao.get(id);
        TstCase testCase = caseDao.get(attach.getCaseId(), user.getDefaultPrjId());
        if (testCase == null) {
            return false;
        }

        caseAttachmentDao.delete(id);
        caseHistoryService.saveHistory(user, Constant.CaseAct.delete_attachment, testCase, attach.getName());

        return true;
    }

}

