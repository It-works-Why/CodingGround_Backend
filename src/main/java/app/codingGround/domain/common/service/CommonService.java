package app.codingGround.domain.common.service;

import app.codingGround.global.config.model.UpLoadFileInfo;
import org.springframework.stereotype.Service;

@Service
public class CommonService {

    public UpLoadFileInfo uploadFile(UpLoadFileInfo upLoadFileInfo) {

        return upLoadFileInfo;
    }
}
