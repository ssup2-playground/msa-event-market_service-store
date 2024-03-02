package com.market.store.server.http.controller;

import com.market.store.config.SwaggerConfig;
import com.market.store.domain.entity.ProductInfo;
import com.market.store.domain.service.ProductService;
import com.market.store.server.http.dto.ProductInfoListRes;
import com.market.store.server.http.dto.ProductInfoReq;
import com.market.store.server.http.dto.ProductInfoRes;
import com.market.store.server.http.dto.ProductQuantityReq;
import com.market.store.server.http.dto.ProductQuantityRes;
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
@Tag(name = "Product")
public class ProductController {
  private final String uuidRegExp =
      "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";

  @Autowired private ProductService productService;
  @Autowired private ModelMapper modelMapper;

  @GetMapping(path = "/stores/{storeId}/products")
  ProductInfoListRes listProduct(
      @PathVariable @Pattern(regexp = uuidRegExp) String storeId,
      @RequestParam(value = "offset", defaultValue = "0") int offset,
      @RequestParam(value = "limit", defaultValue = "50") int limit) {
    return convertProductInfoListResToDto(
        productService.listProductInfos(UUID.fromString(storeId), offset / limit, limit));
  }

  @PostMapping(path = "/stores/{storeId}/products")
  @SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_ACCESS_TOKEN)
  ProductInfoRes createProduct(
      @PathVariable @Pattern(regexp = uuidRegExp) String storeId,
      @RequestBody @Valid ProductInfoReq request) {
    // Set request
    ProductInfo req = convertProductInfoReqToModel(request);
    req.setStoreId(UUID.fromString(storeId));

    // Create product
    ProductInfo resProductInfo = productService.createProductInfo(req);
    return convertProductInfoResToDto(resProductInfo);
  }

  @GetMapping(path = "/stores/{storeId}/products/{productId}")
  ProductInfoRes getProduct(
      @PathVariable @Pattern(regexp = uuidRegExp) String storeId,
      @PathVariable @Pattern(regexp = uuidRegExp) String productId) {
    ProductInfo res =
        productService.getProductInfo(UUID.fromString(storeId), UUID.fromString(productId));
    return convertProductInfoResToDto(res);
  }

  @PutMapping(path = "/stores/{storeId}/products/{productId}")
  @SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_ACCESS_TOKEN)
  void updateProduct(
      @PathVariable @Pattern(regexp = uuidRegExp) String storeId,
      @PathVariable @Pattern(regexp = uuidRegExp) String productId,
      @RequestBody @Valid ProductInfoReq request) {
    ProductInfo req = convertProductInfoReqToModel(request);
    req.setStoreId(UUID.fromString(storeId));
    req.setId(UUID.fromString(productId));
    productService.updateProductInfo(req);
  }

  @DeleteMapping(path = "/stores/{storeId}/products/{productId}")
  @SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_ACCESS_TOKEN)
  void deleteProduct(
      @PathVariable @Pattern(regexp = uuidRegExp) String storeId,
      @PathVariable @Pattern(regexp = uuidRegExp) String productId) {
    productService.deleteProductInfo(UUID.fromString(storeId), UUID.fromString(productId));
  }

  @PostMapping(path = "/stores/{storeId}/products/{productId}/quantity/increase")
  @SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_ACCESS_TOKEN)
  ProductQuantityRes increaseProductQuantity(
      @PathVariable @Pattern(regexp = uuidRegExp) String storeId,
      @PathVariable @Pattern(regexp = uuidRegExp) String productId,
      @RequestBody @Valid ProductQuantityReq request) {
    return convertProductQuantityResToDto(
        productId,
        productService.increaseProductQuantity(
            UUID.fromString(storeId), UUID.fromString(productId), request.getQuantity()));
  }

  @PostMapping(path = "/stores/{storeId}/products/{productId}/quantity/decrease")
  @SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_ACCESS_TOKEN)
  ProductQuantityRes decreaseProductQuantity(
      @PathVariable @Pattern(regexp = uuidRegExp) String storeId,
      @PathVariable @Pattern(regexp = uuidRegExp) String productId,
      @RequestBody @Valid ProductQuantityReq request) {
    return convertProductQuantityResToDto(
        storeId,
        productService.decreaseProductQuantity(
            UUID.fromString(storeId), UUID.fromString(productId), request.getQuantity()));
  }

  public ProductInfo convertProductInfoReqToModel(ProductInfoReq request) {
    return modelMapper.map(request, ProductInfo.class);
  }

  public ProductInfoRes convertProductInfoResToDto(ProductInfo productInfo) {
    return modelMapper.map(productInfo, ProductInfoRes.class);
  }

  public ProductInfoListRes convertProductInfoListResToDto(List<ProductInfo> productInfoList) {
    // Make up product info response list
    List<ProductInfoRes> productInfoResList = new ArrayList<>();
    Iterator<ProductInfo> iter = productInfoList.iterator();
    while (iter.hasNext()) {
      productInfoResList.add(convertProductInfoResToDto(iter.next()));
    }

    // Return response
    return new ProductInfoListRes(productInfoList);
  }

  public ProductQuantityRes convertProductQuantityResToDto(String productId, int productQuantity) {
    return new ProductQuantityRes(productId, productQuantity);
  }
}
