package com.market.store.domain.service;

import com.market.store.domain.entity.Inbox;
import com.market.store.domain.entity.StoreInfo;
import com.market.store.domain.repository.ProductInfoPrimaryRepo;
import com.market.store.domain.repository.StoreInfoPrimaryRepo;
import com.market.store.domain.vo.UserInfo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManagementServiceImp implements ManagementService {

  @Autowired private StoreInfoPrimaryRepo storeInfoPrimaryRepo;
  @Autowired private ProductInfoPrimaryRepo productInfoPrimaryRepo;

  @Override
  @Transactional
  public void deleteStoreProudctByDeletedUserMq(Inbox inbox) {
    // Get userinfo from inbox payload
    UserInfo userInfo = new UserInfo(inbox.getPayload());

    // Save inbox to prevent duplicate action
    // Saving inbox isn't necessary action bacause delete action could be repeatedly performed
    // inboxPrimaryRepo.save(inbox);

    // Delete all products ownedd by deleted user
    List<StoreInfo> storeList = storeInfoPrimaryRepo.findByUserId(userInfo.getId());
    for (StoreInfo storeInfo : storeList) {
      productInfoPrimaryRepo.deleteByStoreId(storeInfo.getId());
    }

    // Delete all stores owned by deleted user
    storeInfoPrimaryRepo.deleteByUserId(userInfo.getId());
  }
}
