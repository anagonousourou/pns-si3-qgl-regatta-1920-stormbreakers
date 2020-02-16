package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Wind;

public class WeatherAnalystTest {
    

    WeatherAnalyst seaElements;
    @BeforeEach
    void setUp(){
        seaElements=new WeatherAnalyst(null, null, null);
    }

    @Test
    void additionalSpeedExistsTest(){
        this.seaElements = new WeatherAnalyst(null, null, null);
        assertFalse(this.seaElements.additionalSpeedExists(),"Pas de vent donc pas de vitesse additionelle");

        this.seaElements = new WeatherAnalyst(new Wind(1.253, 200), null, null);
        assertTrue(this.seaElements.additionalSpeedExists(),"Du vent donc potentiellement vitesse supplémentaire");
    }


    @Test
    void currentExternalSpeedTest(){
        Boat boat =mock(Boat.class);
        Wind wind =mock(Wind.class);
        EquipmentManager equipmentManager=mock(EquipmentManager.class);
        when(wind.getStrength()).thenReturn(200.0);
        when(wind.getOrientation()).thenReturn(0.785398163397448);
        when(boat.getOrientation()).thenReturn(1.570796326794896);
        when(equipmentManager.nbSails()).thenReturn(2);
        when(equipmentManager.nbOpennedSails()).thenReturn(1);
        this.seaElements = new WeatherAnalyst(wind, boat, equipmentManager);
        assertTrue(Math.abs(this.seaElements.currentExternalSpeed()-70.71054) < 0.001,"La vitesse doit etre proche de 70.7105 ");

        when(equipmentManager.nbSails()).thenReturn(0);
        assertEquals(0.0,this.seaElements.currentExternalSpeed() ,"Pas de voile donc pas de vitesse procuree par le vent");
    }

    @Test
    void potentialSpeedAcquirableTest(){
        Boat boat =mock(Boat.class);
        Wind wind =mock(Wind.class);
        EquipmentManager equipmentManager=mock(EquipmentManager.class);

        when(wind.getStrength()).thenReturn(200.0);
        when(wind.getOrientation()).thenReturn(0.785398163397448);
        when(boat.getOrientation()).thenReturn(0.785398163397448);
        when(equipmentManager.nbSails()).thenReturn(2);

        this.seaElements = new WeatherAnalyst(wind, boat, equipmentManager);

        assertEquals(200.0, this.seaElements.potentialSpeedAcquirable(), "");
    }

    @Test
    void externalSpeedGivenNbOpennedSailsTest(){
        Boat boat =mock(Boat.class);
        Wind wind =mock(Wind.class);
        EquipmentManager equipmentManager=mock(EquipmentManager.class);

        when(wind.getStrength()).thenReturn(360.33);
        when(wind.getOrientation()).thenReturn(0.785398163397448);
        when(boat.getOrientation()).thenReturn(0.785398163397448);
        when(equipmentManager.nbSails()).thenReturn(3);

        this.seaElements = new WeatherAnalyst(wind, boat, equipmentManager);

        assertTrue(Math.abs(120.11 -seaElements.externalSpeedGivenNbOpennedSails(1))<=0.001);
    }
}