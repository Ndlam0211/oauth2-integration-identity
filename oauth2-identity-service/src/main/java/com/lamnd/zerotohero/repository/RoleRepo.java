package com.lamnd.zerotohero.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lamnd.zerotohero.entity.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, String> {}
