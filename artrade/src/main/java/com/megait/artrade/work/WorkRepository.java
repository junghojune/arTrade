package com.megait.artrade.work;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkRepository extends JpaRepository<Work, Long> {
  Optional<Work> findByTitle(String title);
}