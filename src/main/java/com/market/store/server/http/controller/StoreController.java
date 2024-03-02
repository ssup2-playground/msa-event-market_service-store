package com.market.store.server.http.controller;

import com.market.store.config.SwaggerConfig;
import com.market.store.domain.entity.StoreInfo;
import com.market.store.domain.service.StoreService;
import com.market.store.server.http.dto.StoreInfoListRes;
import com.market.store.server.http.dto.StoreInfoReq;
import com.market.store.server.http.dto.StoreInfoRes;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping(path = "/v1")
@Tag(name = "Store")
public class StoreController {
  private final String uuidRegExp =
      "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";

  @Autowired private StoreService storeService;
  @Autowired private ModelMapper modelMapper;

  @GetMapping(path = "/stores")
  StoreInfoListRes listStore(
      @RequestParam(value = "offset", defaultValue = "0") int offset,
      @RequestParam(value = "limit", defaultValue = "50") int limit) {
    return convertStoreInfoListResToDto(storeService.listStoreInfos(offset / limit, limit));
  }

  @PostMapping(path = "/stores")
  @SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_ACCESS_TOKEN)
  StoreInfoRes createStore(@RequestBody @Valid StoreInfoReq request) {
    StoreInfo req = convertStoreInfoReqToModel(request);
    StoreInfo res = storeService.createStoreInfo(req);
    return convertStoreInfoResToDto(res);
  }

  @GetMapping(path = "/stores/{storeId}")
  StoreInfoRes getStore(@PathVariable @Pattern(regexp = uuidRegExp) String storeId) {
    StoreInfo resStoreInfo = storeService.getStoreInfo(UUID.fromString(storeId));
    return convertStoreInfoResToDto(resStoreInfo);
  }

  @PutMapping(path = "/stores/{storeId}")
  @SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_ACCESS_TOKEN)
  void updateStore(
      @PathVariable @Pattern(regexp = uuidRegExp) String storeId,
      @RequestBody @Valid StoreInfoReq request) {
    StoreInfo reqProductInfo = convertStoreInfoReqToModel(request);
    reqProductInfo.setId(UUID.fromString(storeId));
    storeService.updateStoreInfo(reqProductInfo);
  }

  @DeleteMapping(path = "/stores/{storeId}")
  @SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_ACCESS_TOKEN)
  void deleteStore(@PathVariable @Pattern(regexp = uuidRegExp) String storeId) {
    storeService.deleteStoreInfo(UUID.fromString(storeId));
  }

  public StoreInfo convertStoreInfoReqToModel(StoreInfoReq request) {
    return modelMapper.map(request, StoreInfo.class);
  }

  public StoreInfoRes convertStoreInfoResToDto(StoreInfo storeInfo) {
    return modelMapper.map(storeInfo, StoreInfoRes.class);
  }

  public StoreInfoListRes convertStoreInfoListResToDto(List<StoreInfo> storeInfoList) {
    // Make up store info response list
    List<StoreInfoRes> storeInfoResList = new ArrayList<>();
    Iterator<StoreInfo> iter = storeInfoList.iterator();
    while (iter.hasNext()) {
      storeInfoResList.add(convertStoreInfoResToDto(iter.next()));
    }
    // Return response:
    return new StoreInfoListRes(storeInfoResList);
  }
}
