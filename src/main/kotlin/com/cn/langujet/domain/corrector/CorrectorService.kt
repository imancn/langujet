package com.cn.langujet.domain.corrector

import com.cn.langujet.actor.corrector.payload.response.CorrectorProfileResponse
import com.cn.langujet.application.service.users.Auth
import com.cn.langujet.application.arch.advice.UnprocessableException
import com.cn.langujet.application.arch.mongo.HistoricalEntityService
import org.springframework.stereotype.Service

@Service
class CorrectorService(
    override var repository: CorrectorRepository,
) : HistoricalEntityService<CorrectorRepository, CorrectorEntity>() {
    
    fun editProfile(
        fullName: String?,
        biography: String?,
        ieltsScore: Double?,
    ): CorrectorProfileResponse {
        val corrector = this.getCorrectorByUserId(Auth.userId())
        
        if (fullName.isNullOrBlank() && biography.isNullOrBlank() && ieltsScore == null) return CorrectorProfileResponse(
            corrector
        )
        
        if (!fullName.isNullOrBlank()) corrector.fullName = fullName
        if (!biography.isNullOrBlank()) corrector.biography = biography
        ieltsScore?.let { corrector.ieltsScore = it }
        
        return CorrectorProfileResponse(save(corrector))
    }
    
    fun getCorrectorByUserId(userId: Long): CorrectorEntity {
        return repository.findByUser_Id(userId).orElseThrow {
            UnprocessableException("Corrector not found")
        }
    }
    
    fun correctorExistsByUserId(userId: Long): Boolean {
        return repository.existsByUser_Id(userId)
    }
}