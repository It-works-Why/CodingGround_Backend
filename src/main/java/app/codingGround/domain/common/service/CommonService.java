package app.codingGround.domain.common.service;

import app.codingGround.domain.common.repository.CommonRepository;
import app.codingGround.global.config.model.UpLoadFileInfo;
import org.springframework.stereotype.Service;

@Service
public class CommonService {

    private final CommonRepository commonRepository;

    public CommonService(CommonRepository commonRepository) {
        this.commonRepository = commonRepository;
    }


    public UpLoadFileInfo uploadFile(UpLoadFileInfo upLoadFileInfo) {

        return upLoadFileInfo;
    }
}
