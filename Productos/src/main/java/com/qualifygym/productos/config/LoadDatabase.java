package com.qualifygym.productos.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.qualifygym.productos.model.Producto;
import com.qualifygym.productos.repository.ProductoRepository;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(ProductoRepository productoRepo) {
        return args -> {
            // Crear productos iniciales solo si no existen
            if (productoRepo.count() == 0) {
                // Producto 1: Proteína Whey
                Producto proteina = new Producto();
                proteina.setNombre("Proteína Whey");
                proteina.setDescripcion("Proteína de suero de leche de alta calidad para recuperación muscular. Ideal para después del entrenamiento.");
                proteina.setPrecio(49.99);
                proteina.setCategoria("supplement");
                proteina.setImagen("https://via.placeholder.com/300x300?text=Proteina+Whey");
                proteina.setStock(50);
                productoRepo.save(proteina);

                // Producto 2: Creatina Monohidratada
                Producto creatina = new Producto();
                creatina.setNombre("Creatina Monohidratada");
                creatina.setDescripcion("Creatina pura para aumentar fuerza y masa muscular. Aumenta el rendimiento en ejercicios de alta intensidad.");
                creatina.setPrecio(24.99);
                creatina.setCategoria("supplement");
                creatina.setImagen("https://via.placeholder.com/300x300?text=Creatina");
                creatina.setStock(75);
                productoRepo.save(creatina);

                // Producto 3: Cinturón de Pesas
                Producto cinturon = new Producto();
                cinturon.setNombre("Cinturón de Pesas");
                cinturon.setDescripcion("Cinturón de cuero resistente para levantamiento de pesas. Proporciona soporte lumbar durante ejercicios pesados.");
                cinturon.setPrecio(29.99);
                cinturon.setCategoria("accessory");
                cinturon.setImagen("https://via.placeholder.com/300x300?text=Cinturon");
                cinturon.setStock(30);
                productoRepo.save(cinturon);

                System.out.println("✅ 3 productos precargados creados en la base de datos");
            } else {
                System.out.println("ℹ Productos ya existen. No se cargaron nuevos datos.");
            }
        };
    }
}

