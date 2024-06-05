package com.popov.fintrack.web.mapper;

import com.popov.fintrack.wallet.dto.WalletDTO;
import com.popov.fintrack.wallet.model.Wallet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletMapper extends Mappable<Wallet, WalletDTO> {
}
