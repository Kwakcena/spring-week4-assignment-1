package com.codesoom.assignment.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.codesoom.assignment.domain.ProductConstant.TITLE;

import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.ProductNotFoundException;
import com.codesoom.assignment.domain.Product;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@DisplayName("장난감 리소스")
public final class ProductControllerWebTest {
    private static final String EMPTY_LIST = "[]";

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("전체 목록 조회 엔드포인트는")
    class Describe_products_get {
        @Nested
        @DisplayName("전체 목록 요청 시")
        class Context_request_product_list {
            @Nested
            @DisplayName("저장된 데이터가 없다면")
            class Context_product_empty {
                @Test
                @DisplayName("빈 목록을 리턴한다.")
                void it_returns_a_empty_list() throws Exception {
                    mockMvc.perform(get("/products"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(EMPTY_LIST));
                }
            }
        }
    }

    @Nested
    @DisplayName("검색 엔드포인트는")
    class Describe_products_id_get {
        @Nested
        @DisplayName("장난감 데이터 요청 시")
        class Contest_request_product {
            @Nested
            @DisplayName("요청한 장난감을 찾을 수 있다면")
            class Context_find_success {
                @BeforeEach
                void setUp() {
                    when(productService.detailProduct(anyLong()))
                        .thenReturn(new Product(anyString()));
                }

                @Test
                @DisplayName("찾은 장난감을 리턴한다.")
                void it_returns_a_find_product() throws Exception {
                    mockMvc.perform(get("/products/1"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(TITLE)));
                }
            }

            @Nested
            @DisplayName("요청한 장난감을 찾을 수 없다면")
            class Context_find_fail {
                @BeforeEach
                void setUp() {
                when(productService.detailProduct(anyLong()))
                    .thenThrow(new ProductNotFoundException(anyLong()));
                }

                @Test
                @DisplayName("404(NOT FOUND) http status code를 리턴한다.")
                void it_notify_a_find_fail() throws Exception {
                    mockMvc.perform(get("/products/1"))
                        .andExpect(status().isNotFound());
                }
            }

            @AfterEach
            void tearDown() {
                verify(productService)
                    .detailProduct(anyLong());
            }
        }
    }

    @Nested
    @DisplayName("생성 엔드포인트는")
    class Describe_products_post {
        @BeforeEach
        void setUp() {
            when(productService.createProduct(any(Product.class)))
                .thenReturn(new Product(anyString()));
        }

        @Nested
        @DisplayName("장난감 생성 요청 시")
        class Context_request_product_create {
            @Test
            @DisplayName("장난감을 생성하고 리턴한다.")
            void it_returns_a_product() throws Exception {
                mockMvc.perform(
                        post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"title\"}")
                    )
                    .andExpect(status().isCreated())
                    .andExpect(content().string(containsString(TITLE)));
            }
        }

        @AfterEach
        void tearDown() {
            verify(productService)
                .createProduct(any(Product.class));
        }
    }
}