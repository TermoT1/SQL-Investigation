package ru.sqlinvestigation.RestAPI.repositories.gameDB;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sqlinvestigation.RestAPI.models.gameDB.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
