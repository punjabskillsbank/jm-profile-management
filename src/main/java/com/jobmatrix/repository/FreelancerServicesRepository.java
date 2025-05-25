package com.jobmatrix.repository;


import com.jobmatrix.entity.FreelancerService;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface FreelancerServicesRepository extends JpaRepository<FreelancerService, Long> {

    @Query("SELECT fs FROM FreelancerService fs WHERE fs.id.freelancerId = :freelancerId")
    List<FreelancerService> findByFreelancerId_FreelancerId(@Param("freelancerId") UUID freelancerId);

    /*
So what's happening in the code below is that whenever we are using this POST method for that particular freelancerId, it will delete all the services that are associated with that freelancerId and will again add it. Sounds like a PUT method. For example some freelancer has 3 services and he wants to add 2 more services, so we will delete all the previous services and add the new ones. So it will be like a PUT method. Like it will now add all the 5 data in the database again. (This comment is just for clarification purpose. Can be removed later if not needed.)
*/
    @Modifying
    @Transactional
    @Query("DELETE FROM FreelancerService fs WHERE fs.id.freelancerId = :freelancerId")
    void deleteByFreelancerId(@Param("freelancerId") UUID freelancerId);

}
