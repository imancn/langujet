package com.cn.langujet.application.service.cms.contents

import com.cn.langujet.application.arch.mongo.HistoricalEntityService
import org.springframework.stereotype.Service

@Service
class ContentService(
    override var repository: ContentRepository
) : HistoricalEntityService<ContentRepository, ContentEntity>()