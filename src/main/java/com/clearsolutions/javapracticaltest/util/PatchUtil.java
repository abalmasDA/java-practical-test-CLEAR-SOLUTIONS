package com.clearsolutions.javapracticaltest.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PatchUtil {

  private final ObjectMapper objectMapper;

  /**
   * Applies a JSON patch to a target DTO object.
   *
   * @param patchData  The JSON patch data to apply
   * @param targetBean The target DTO object to apply the patch to
   * @param beanClass  The class of the target DTO object
   * @param <T>        The type of the target DTO object
   * @return The patched DTO object
   */
  @SneakyThrows
  public <T> T applyPatch(JsonPatch patchData, T targetBean, Class<T> beanClass) {
    JsonNode target = objectMapper.convertValue(targetBean, JsonNode.class);
    JsonNode patched = patchData.apply(target);
    return objectMapper.convertValue(patched, beanClass);
  }

}