package com.resumematch.service;

import com.resumematch.common.R;

public interface FavoriteService {

    R addFavorite(Long userId, Long jobId);

    R removeFavorite(Long userId, Long jobId);

    R listMyFavorites(Long userId, Integer page, Integer size);

    R isFavorited(Long userId, Long jobId);
}
