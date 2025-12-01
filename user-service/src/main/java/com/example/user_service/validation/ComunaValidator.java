package com.example.user_service.validation;

import com.example.user_service.model.Region;
import com.example.user_service.model.UserProfile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComunaValidator implements ConstraintValidator<ValidComuna, UserProfile> {

    private static final Map<Region, List<String>> comunasPorRegion = new HashMap<>();
    static {
        comunasPorRegion.put(Region.ARICA_Y_PARINACOTA, Arrays.asList("Arica", "Camarones", "Putre", "General Lagos"));
        comunasPorRegion.put(Region.TARAPACA, Arrays.asList("Iquique", "Alto Hospicio", "Pozo Almonte", "Camiña", "Colchane", "Huara", "Pica"));
        comunasPorRegion.put(Region.ANTOFAGASTA, Arrays.asList("Antofagasta", "Mejillones", "Sierra Gorda", "Taltal"));
        comunasPorRegion.put(Region.ATACAMA, Arrays.asList("Copiapó", "Caldera", "Tierra Amarilla", "Chañaral", "Diego de Almagro"));
        comunasPorRegion.put(Region.COQUIMBO, Arrays.asList("La Serena", "Coquimbo", "Andacollo", "Illapel", "Los Vilos", "Ovalle", "Combarbalá", "Salamanca", "Monte Patria", "Punitaqui", "Río Hurtado"));
        comunasPorRegion.put(Region.VALPARAISO, Arrays.asList("Valparaíso", "Viña del Mar", "Quilpué", "Villa Alemana", "Concón", "Quintero", "Olmué", "Limache", "Casablanca", "San Antonio", "Cartagena", "El Quisco", "El Tabo", "Santo Domingo"));
        comunasPorRegion.put(Region.METROPOLITANA_DE_SANTIAGO, Arrays.asList("Santiago", "Providencia", "Las Condes", "La Florida", "Maipú", "Puente Alto", "Ñuñoa", "Recoleta", "Vitacura", "Lo Barnechea", "Pudahuel", "Independencia"));
        comunasPorRegion.put(Region.O_HIGGINS, Arrays.asList("Rancagua", "San Fernando", "Santa Cruz", "Machalí", "Graneros", "Rengo", "San Vicente"));
        comunasPorRegion.put(Region.MAULE, Arrays.asList("Talca", "Curicó", "Linares", "Parral", "Constitución", "Molina", "San Javier"));
        comunasPorRegion.put(Region.ÑUBLE, Arrays.asList("Chillán", "Chillán Viejo", "Quillón", "San Carlos", "Bulnes", "Pemuco", "Coihueco"));
        comunasPorRegion.put(Region.BIOBIO, Arrays.asList("Concepción", "Talcahuano", "Chiguayante", "Hualpén", "Los Ángeles", "San Pedro de la Paz", "Coronel", "Lota"));
        comunasPorRegion.put(Region.LA_ARAUCAÑIA, Arrays.asList("Temuco", "Villarrica", "Padre Las Casas", "Pitrufquén", "Carahue", "Nueva Imperial", "Angol", "Victoria"));
        comunasPorRegion.put(Region.LOS_RIOS, Arrays.asList("Valdivia", "La Unión", "Rio Bueno", "Paillaco", "Futrono", "Lanco"));
        comunasPorRegion.put(Region.LOS_LAGOS, Arrays.asList("Puerto Montt", "Puerto Varas", "Osorno", "Castro", "Ancud", "Quellón", "Calbuco", "Chaitén"));
        comunasPorRegion.put(Region.AYSEN, Arrays.asList("Coyhaique", "Puerto Aysén", "Chile Chico", "Cisnes", "Guaitecas", "Aysén", "Río Ibáñez"));
        comunasPorRegion.put(Region.MAGALLANES_Y_ANTARTICA, Arrays.asList("Punta Arenas", "Puerto Natales", "Puerto Williams", "Porvenir", "Cabo de Hornos", "Laguna Blanca", "San Gregorio"));
    }


    @Override
    public boolean isValid(UserProfile userProfile, ConstraintValidatorContext context) {
        if(userProfile.getRegion() == null || userProfile.getComuna() == null) return false;
        List<String> comunas = comunasPorRegion.get(userProfile.getRegion());
        return comunas != null && comunas.contains(userProfile.getComuna());
    }
}
