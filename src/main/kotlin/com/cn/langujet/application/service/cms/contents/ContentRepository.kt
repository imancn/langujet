package com.cn.langujet.application.service.cms.contents

import com.cn.langujet.application.arch.mongo.HistoricalMongoRepository

interface ContentRepository : HistoricalMongoRepository<ContentEntity>
