package com.popov.fintrack.email.event;

public record ExclusionEvent(Long userId, Long walletId) implements AppEvent {}
