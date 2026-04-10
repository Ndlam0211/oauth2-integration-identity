package com.lamnd.zerotohero.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lamnd.zerotohero.entity.Permission;

@Repository
public interface PermissionRepo extends JpaRepository<Permission, String> {}
