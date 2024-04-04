package com.cn.langujet.domain.professor

import com.cn.langujet.actor.professor.payload.response.ProfessorProfileResponse
import com.cn.langujet.application.advice.NotFoundException
import com.cn.langujet.domain.user.services.AuthService
import org.springframework.stereotype.Service

@Service
class ProfessorService(
    private val authService: AuthService,
    private val professorRepository: ProfessorRepository,
) {

    fun editProfile(auth: String?, fullName: String?, biography: String?, ieltsScore: Double?, credit: Double?): ProfessorProfileResponse {
        if (auth.isNullOrBlank()) throw IllegalArgumentException("Auth token cannot be null or blank")

        val professor = this.getProfessorByAuthToken(auth)

        if (fullName.isNullOrBlank() && biography.isNullOrBlank() && ieltsScore == null && credit == null)
            return ProfessorProfileResponse(professor)

        if (!fullName.isNullOrBlank()) professor.fullName = fullName
        if (!biography.isNullOrBlank()) professor.biography = biography
        ieltsScore?.let { professor.ieltsScore = it }
        credit?.let { professor.credit = it }

        return ProfessorProfileResponse(professorRepository.save(professor))
    }

    fun getProfessorByAuthToken(auth: String?): Professor {
        val userId = authService.getUserByAuthToken(auth).id
            ?: throw NotFoundException("User not found for given auth token")

        return findProfessorByUserId(userId)
    }

    fun getProfessorByUserId(userId: String): Professor {
        val validUserId = authService.getUserById(userId).id
            ?: throw NotFoundException("User not found for given user ID")

        return findProfessorByUserId(validUserId)
    }

    private fun findProfessorByUserId(userId: String): Professor {
        return professorRepository.findByUser_Id(userId)
            .orElseThrow { NotFoundException("Professor not found") }
    }

    fun doesProfessorOwnAuthToken(token: String, professorId: String): Boolean {
        return getProfessorByAuthToken(token).user.id == professorId
    }
}