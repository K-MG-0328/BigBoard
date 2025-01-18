package com.github.mingyu.bigboard.repository;

import com.github.mingyu.bigboard.entitiy.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {
}
