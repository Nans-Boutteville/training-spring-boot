package com.ecommerce.microcommerce.Controllers;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.web.controller.ProductController;
import com.ecommerce.microcommerce.web.exceptions.ProduitGratuitException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {
    @Autowired
    private ProductController controller;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductDao productDao;

    private Product produitTablette, produitOrdinateur, produitGratuit;

    @Autowired
    ObjectMapper objectMapper;

    @Before
    public void initProduct(){
        this.produitTablette = new Product(1, "tablette", 250, 200);
        this.produitOrdinateur = new Product(2, "Ordinateur Gamer", 1400, 1400);
        this.produitGratuit = new Product(3, "steam", 0, 0);
    }

    @Test
    public void afficherUnProduitTest() throws Exception {
        given(productDao.findById(produitTablette.getId())).willReturn(produitTablette);

        mockMvc.perform(get("/Produits/"+produitTablette.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(produitTablette.getId())))
                .andExpect(jsonPath("$.nom", is(produitTablette.getNom())))
                .andExpect(jsonPath("$.prix", is(produitTablette.getPrix())))
                .andExpect(jsonPath("$.prixAchat", is(produitTablette.getPrixAchat())));
    }

    @Test
    public void ajouterProduitTest() throws Exception {
        given(productDao.save(produitTablette)).willReturn(produitTablette);
        this.mockMvc.perform(post("/Produits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produitTablette)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test(expected = ProduitGratuitException.class)
    public void ajouterProduitExceptionTest() throws Exception {
        controller.ajouterProduit(produitGratuit);
    }

    @Test
    public void calculerMargeProduitTest() throws Exception {
        List<Product> listeProduits = new ArrayList<>();
        listeProduits.add(produitTablette);
        listeProduits.add(produitOrdinateur);

        given(productDao.findAll()).willReturn(listeProduits);
        mockMvc.perform(get("/AdminProduits")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[\"Product{id=1, nom='tablette', prix=250}:50\",\"Product{id=2, nom='Ordinateur Gamer', prix=1400}:0\"]"));
    }

    @Test
    public void testeDeRequetesTest() throws Exception {
        List<Product> listeProduits = new ArrayList<>();
        listeProduits.add(produitOrdinateur);

        given(productDao.chercherUnProduitCher(400)).willReturn(listeProduits);
        mockMvc.perform(get("/test/produits/"+400)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(produitOrdinateur.getId())))
                .andExpect(jsonPath("$[0].nom", is(produitOrdinateur.getNom())))
                .andExpect(jsonPath("$[0].prix", is(produitOrdinateur.getPrix())))
                .andExpect(jsonPath("$[0].prixAchat", is(produitOrdinateur.getPrixAchat())));
    }
    @Test
    public void listeProduitsTest() throws Exception {
        List<Product> listeProduits = new ArrayList<>();
        listeProduits.add(produitTablette);
        listeProduits.add(produitOrdinateur);

        given(productDao.findAll()).willReturn(listeProduits);

        mockMvc.perform(get("/Produits")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(produitTablette.getId())))
                .andExpect(jsonPath("$[0].nom", is(produitTablette.getNom())))
                .andExpect(jsonPath("$[0].prix", is(produitTablette.getPrix())))
                .andExpect(jsonPath("$[0].prixAchat", is(produitTablette.getPrixAchat())));
    }

    @Test
    public void trierProduitsParOrdreAlphabetiqueTest() throws Exception {
        List<Product> listeProduits = new ArrayList<>();
        listeProduits.add(produitTablette);
        listeProduits.add(produitOrdinateur);

        given(productDao.trierProduitsParOrdreAlphabetique()).willReturn(listeProduits);

        mockMvc.perform(get("/Produits/trie")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(produitTablette.getId())))
                .andExpect(jsonPath("$[0].nom", is(produitTablette.getNom())))
                .andExpect(jsonPath("$[0].prix", is(produitTablette.getPrix())))
                .andExpect(jsonPath("$[0].prixAchat", is(produitTablette.getPrixAchat())))
                .andExpect(jsonPath("$[1].id", is(produitOrdinateur.getId())))
                .andExpect(jsonPath("$[1].nom", is(produitOrdinateur.getNom())))
                .andExpect(jsonPath("$[1].prix", is(produitOrdinateur.getPrix())))
                .andExpect(jsonPath("$[1].prixAchat", is(produitOrdinateur.getPrixAchat())));
    }
}
