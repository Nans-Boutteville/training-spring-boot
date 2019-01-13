package com.ecommerce.microcommerce;


import com.ecommerce.microcommerce.model.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductTest {

    private Product produit;

    @Before
    public void initProduct(){
        this.produit = new Product(1, "tablette", 200, 200);
    }

    @Test
    public void toStringTest(){
        String r ="Product{" +
                "id=" + produit.getId() +
                ", nom='" + produit.getNom() + '\'' +
                ", prix=" + produit.getPrix() +
                '}';
        assertEquals(r,produit.toString());
    }
}
