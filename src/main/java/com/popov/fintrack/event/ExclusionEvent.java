package com.popov.fintrack.event;

public record ExclusionEvent(Long userId, Long walletId) implements AppEvent {}
