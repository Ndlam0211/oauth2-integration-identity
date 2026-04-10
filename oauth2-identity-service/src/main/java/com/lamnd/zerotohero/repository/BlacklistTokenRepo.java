package com.lamnd.zerotohero.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lamnd.zerotohero.entity.BlacklistToken;

public interface BlacklistTokenRepo extends JpaRepository<BlacklistToken, String> {}
