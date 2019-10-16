package objects;

import models.ER_Vehicle_LoJack;

import java.math.BigDecimal;

public class LoJackOptions {

    public Integer number;
    public String description;
    public BigDecimal recharge;
    public BigDecimal overprime;
    public Integer operation;

    public static String getDescription(int i) {
        String description = "";
        switch (i) {
            case 1:
                description = "Dispositivo Pagado por Cliente";
                break;
            case 2:
                description =  "Sin Dispositivo Instalado";
                break;
            case 3:
                description =  "Sin Dispositivo, 10% en Prima Neta";
                break;
            case 4:
                description =  "Sin Dispositivo, 15% en Prima Neta";
                break;
         /*   case 5:
                description =  "Sin Dispositivo, Q500 en Prima Neta";
                break;
            case 6:
                description =  "Sin Dispositivo, Q850 en Prima Neta";
            break;*/
        }
        return description;
    }

    public static BigDecimal getRecharge(int i, ER_Vehicle_LoJack loJack) {
        BigDecimal recharge = new BigDecimal(0);
        switch (i) {
            case 1:
                recharge = loJack.withLoJack;
                break;
            case 2:
                recharge = loJack.withoutLoJack;
                break;
            case 3:
                recharge = loJack.withoutLoJackPlus10;
                break;
            case 4:
                recharge = loJack.withoutLoJackPlus15;
                break;
            case 5:
                recharge = loJack.withoutLoJackPlusHalf;
                break;
            case 6:
                recharge = loJack.withoutLoJackPlusFull;
                break;
        }
        return recharge;
    }

    public static BigDecimal getOverprime(int i, ER_Vehicle_LoJack loJack) {
        BigDecimal overprime = new BigDecimal(0);
        switch (i) {
            case 3:
                overprime = new BigDecimal(10);
                break;
            case 4:
                overprime = new BigDecimal(15);
                break;
            case 5:
                overprime = new BigDecimal(500);
                break;
            case 6:
                overprime = new BigDecimal(850);
                break;
        }
        return overprime;
    }

    public static Integer getOperation(int i) {
        Integer operation = 0;
        switch (i) {
            case 3:
            case 4:
                operation = 1;
                break;
            case 5:
            case 6:
                operation = 2;
                break;
        }
        return operation;
    }

    public static LoJackOptions find(Integer loJack) {
        return null;
    }
}
