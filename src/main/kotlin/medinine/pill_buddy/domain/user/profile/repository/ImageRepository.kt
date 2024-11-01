package medinine.pill_buddy.domain.user.profile.repository

import medinine.pill_buddy.domain.user.profile.entity.Image
import org.springframework.data.jpa.repository.JpaRepository

interface ImageRepository : JpaRepository<Image, Long>