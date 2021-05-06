/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizador.Auxiliar;

/**
 *
 * @author marco
 */
public class AuxiliarSintaxis {

    public int[][] obtenerArregloProducciones() {
        int producciones[][] = {
            {},//0
            {-48, 802, 204, 250, 801, -51, 201}, //1
            {201, -45, 804, 200, 8032, -13, 203, 803, -53, -6, 901, -111},//2
            {201, -45, 211, -42, -6},//3
            {205, -6, 8033},//4 
            {204, 250, -45, 920},//5
            {205, -6, 8033, -46},//6
            {207},//7
            {208, 209},//8
            {207, -20},//9   *
            {207, -24},//10  /
            {207, -26},//11        //
            {207, -28},//12  %
            {210},//13
            {370, 226},//14
            {370, 226, 210, -22},//15
            {212, 900},//16
            {-8, 809},//17 Flotante
            {-4, 810},//18 Cadena
            {-1, 811},//19 Caracter
            {213},//20
            {-9, 812},//21 Compleja
            {-81, 813},//22 Booleana
            {-82, 813},//23 Booleana
            {218},//24
            {-83, 814},//25 None
            {214},//26
            {-7, 805},//27 Decimal
            {-10, 806},//28 Binario
            {-12, 808},//29 Hexadecimal
            {-11, 807},//30 Octal
            {216},//31
            {217, 206},//32
            {216, -17},//33
            {216, -14},//34
            {219},//35
            {8162, -13, 220, 232, 816, -53},//36
            {365},//37
            {-13, 213, 366, -46, 213, 366, -46, 213, 366, -53, -84, 819},//38
            {8202, -48, 902, 222, 221, 211, 8203, -51, 820},//39
            {220, 232, -46},//40
            {211, 8204, -110},//41
            {222, 221, 211, 8203, -46},//42
            {224},//43
            {225, 260},//44
            {224, -30},//45
            {227},//46
            {211},//47
            {228, -6},//48
            {228, -6, -15},//49
            {228, -6, -18},//50
            {263},//51
            {229, 365},//52
            {230, 256},//53
            {-15},//54
            {-18},//55
            {258, -112},//56
            {230, 256},//57
            {232},//58
            {-13, 231, -53, -85},//59
            {-4},//60
            {233},//61
            {234, 238},//62
            {233, -35},//63
            {236},//64
            {237, 215},//65
            {236, -37},//66
            {236, -40},//67
            {239},//68
            {240, 223},//69
            {239, -33},//70
            {239, -117},//71
            {242},//72
            {243, 235},//73
            {242, -32},//74
            {245},//75
            {246, 247},//76
            {245, -34},//77
            {248},//78
            {249, 241},//79
            {248, -44},//80
            {251},//81
            {-13, 252, 232, -53, -75},//82
            {253, -53, -86},//83
            {254, 255, 250, -110, 1010, 232, -68},//84
            {8402, -87, 255, 250, -110, 232, -116, 232, 8403, -67, 840},//85
            {-88, 255, 250, -110, 1011, 232, -79},//86
            {-59},//87
            {-61},//88
            {232, -77},//89
            {232},//90
            {252, 232, -46},//91
            {-13},//92
            {-13, 252, 232},//93
            {254, 250, -45},//94
            {254, 255, 250, -110, 1012, 232, -62},//95
            {-87, 255, 250, -63},//96
            {-87},//97
            {255, 250, -45},//98
            {257},//99
            {-42},//100
            {-16},//101
            {-27},//102
            {-23},//103
            {-19},//104
            {-25},//105
            {-21},//106
            {-29},//107
            {259},//108
            {-13, -53, -89},//109
            {-13, -53, -90},//110
            {-13, 232, -53, -91},//111
            {-13, 232, -53, -92},//112
            {-13, 232, -53, -93},//113
            {-13, 232, -53, -94},//114
            {-13, 232, -53, -95},//115
            {-13, 232, -53, -96},//116
            {-13, 232, -46, 232, -53, -97},//117
            {261},//118
            {262, 244},//119
            {261, -36},//120
            {261, -38},//121
            {261, -43},//122
            {261, -31},//123
            {261, -41},//124
            {261, -39},//125
            {261, -71},//126
            {261, -72},//127
            {261, -70},//128
            {261, -98},//129
            {264},//130
            {-13, 232, -46, 232, -53, -99},//131
            {-13, 232, -46, 232, -53, -100},//132
            {-13, 232, -53, -101},//133
            {-13, 232, -46, 232, -53, -102},//134
            {-13, 232, -53, -103},//135
            {-13, -53, -104},//136
            {-13, 232, -53, -105},//137
            {-13, 232, -53, -106},//138
            {-13, 232, -53, -107},//139
            {-13, 232, -53, -108},//140
            {-13, 232, -53, -109},//141
            {8152, -47, 366, 232, 10301, -52, 815}, //142  Arreglos
            {366, 232, 10301, 8153, -46},//143             U1 ;
            {366, 232, 10301, 8153, -45}, //144            U1 ,
            {366, 232, 10302, 8153, -110},//145            U1 :
//            {366, 232, 8153, -46},//146
//            {366, 232, 8153, -45} //147
        //                {8152, -47, 367, 232, 366, -52, 815},//141
        //                {-17},//142
        //                {368, 232, 366, -110},//143
        //                {232, 366, -110}//144
        };
        return producciones;
    }
    
    public int valorColumna(int i) {
        int columnaMatrizProducciones = 0;
        switch (i) {
            case -111:
                columnaMatrizProducciones = 1;
                break;
            case -6:
                columnaMatrizProducciones = 2;
                break;
            case -53:
                columnaMatrizProducciones = 3;
                break;
            case -13:
                columnaMatrizProducciones = 4;
                break;
            case -46:
                columnaMatrizProducciones = 5;
                break;
            case -42:
                columnaMatrizProducciones = 6;
                break;
            case -45:
                columnaMatrizProducciones = 7;
                break;
            case -51:
                columnaMatrizProducciones = 8;
                break;
            case -48:
                columnaMatrizProducciones = 9;
                break;
            case -20:
                columnaMatrizProducciones = 10;
                break;
            case -24:
                columnaMatrizProducciones = 11;
                break;
            case -26:
                columnaMatrizProducciones = 12;
                break;
            case -28:
                columnaMatrizProducciones = 13;
                break;
            case -8:
                columnaMatrizProducciones = 14;
                break;
            case -4:
                columnaMatrizProducciones = 15;
                break;
            case -1:
                columnaMatrizProducciones = 16;
                break;
            case -9:
                columnaMatrizProducciones = 17;
                break;
            case -81:
                columnaMatrizProducciones = 18;
                break;
            case -82:
                columnaMatrizProducciones = 19;
                break;
            case -83:
                columnaMatrizProducciones = 20;
                break;
            case -22:
                columnaMatrizProducciones = 21;
                break;
            case -7:
                columnaMatrizProducciones = 22;
                break;
            case -10:
                columnaMatrizProducciones = 23;
                break;
            case -12:
                columnaMatrizProducciones = 24;
                break;
            case -11:
                columnaMatrizProducciones = 25;
                break;
            case -14:
                columnaMatrizProducciones = 26;
                break;
            case -17:
                columnaMatrizProducciones = 27;
                break;
            case -35:
                columnaMatrizProducciones = 28;
                break;
            case -37:
                columnaMatrizProducciones = 29;
                break;
            case -40:
                columnaMatrizProducciones = 30;
                break;
            case -33:
                columnaMatrizProducciones = 31;
                break;
            case -117:
                columnaMatrizProducciones = 32;
                break;
            case -32:
                columnaMatrizProducciones = 33;
                break;
            case -34:
                columnaMatrizProducciones = 34;
                break;
            case -44:
                columnaMatrizProducciones = 35;
                break;
            case -15:
                columnaMatrizProducciones = 36;
                break;
            case -18:
                columnaMatrizProducciones = 37;
                break;
            case -112:
                columnaMatrizProducciones = 38;
                break;
            case -85:
                columnaMatrizProducciones = 39;
                break;
            case -30:
                columnaMatrizProducciones = 40;
                break;
            case -75:
                columnaMatrizProducciones = 41;
                break;
            case -86:
                columnaMatrizProducciones = 42;
                break;
            case -68:
                columnaMatrizProducciones = 43;
                break;
            case -67:
                columnaMatrizProducciones = 44;
                break;
            case -79:
                columnaMatrizProducciones = 45;
                break;
            case -59:
                columnaMatrizProducciones = 46;
                break;
            case -61:
                columnaMatrizProducciones = 47;
                break;
            case -77:
                columnaMatrizProducciones = 48;
                break;
            case -70:
                columnaMatrizProducciones = 49;
                break;
            case -88:
                columnaMatrizProducciones = 50;
                break;
            case -62:
                columnaMatrizProducciones = 51;
                break;
            case -63:
                columnaMatrizProducciones = 52;
                break;
            case -87:
                columnaMatrizProducciones = 53;
                break;
            case -36:
                columnaMatrizProducciones = 54;
                break;
            case -38:
                columnaMatrizProducciones = 55;
                break;
            case -43:
                columnaMatrizProducciones = 56;
                break;
            case -31:
                columnaMatrizProducciones = 57;
                break;
            case -41:
                columnaMatrizProducciones = 58;
                break;
            case -39:
                columnaMatrizProducciones = 59;
                break;
            case -71:
                columnaMatrizProducciones = 60;
                break;
            case -72:
                columnaMatrizProducciones = 61;
                break;
            case -98:
                columnaMatrizProducciones = 62;
                break;
            case -99:
                columnaMatrizProducciones = 63;
                break;
            case -100:
                columnaMatrizProducciones = 64;
                break;
            case -101:
                columnaMatrizProducciones = 65;
                break;
            case -102:
                columnaMatrizProducciones = 66;
                break;
            case -103:
                columnaMatrizProducciones = 67;
                break;
            case -104:
                columnaMatrizProducciones = 68;
                break;
            case -105:
                columnaMatrizProducciones = 69;
                break;
            case -106:
                columnaMatrizProducciones = 70;
                break;
            case -107:
                columnaMatrizProducciones = 71;
                break;
            case -108:
                columnaMatrizProducciones = 72;
                break;
            case -109:
                columnaMatrizProducciones = 73;
                break;
            case -89:
                columnaMatrizProducciones = 74;
                break;
            case -90:
                columnaMatrizProducciones = 75;
                break;
            case -91:
                columnaMatrizProducciones = 76;
                break;
            case -92:
                columnaMatrizProducciones = 77;
                break;
            case -93:
                columnaMatrizProducciones = 78;
                break;
            case -94:
                columnaMatrizProducciones = 79;
                break;
            case -95:
                columnaMatrizProducciones = 80;
                break;
            case -96:
                columnaMatrizProducciones = 81;
                break;
            case -97:
                columnaMatrizProducciones = 82;
                break;
            case -110:
                columnaMatrizProducciones = 83;
                break;
            case -16:
                columnaMatrizProducciones = 84;
                break;
            case -27:
                columnaMatrizProducciones = 85;
                break;
            case -23:
                columnaMatrizProducciones = 86;
                break;
            case -19:
                columnaMatrizProducciones = 87;
                break;
            case -25:
                columnaMatrizProducciones = 88;
                break;
            case -21:
                columnaMatrizProducciones = 89;
                break;
            case -29:
                columnaMatrizProducciones = 90;
                break;
            case -52:
                columnaMatrizProducciones = 91;
                break;
            case -47:
                columnaMatrizProducciones = 92;
                break;
            case -84:
                columnaMatrizProducciones = 93;
                break;
            case -116:
                columnaMatrizProducciones = 94;
                break;
            case -1000:
                columnaMatrizProducciones = 95;
                break;

        }
        if (columnaMatrizProducciones == 0) {
            return columnaMatrizProducciones;
        } else {
            return columnaMatrizProducciones - 1;
        }
    }

    public int valorFila(int i) {
        int filaMatrizProducciones = 0;
        switch (i) {
            case 200:
                filaMatrizProducciones = 1;
                break;
            case 201:
                filaMatrizProducciones = 2;
                break;
            case 203:
                filaMatrizProducciones = 3;
                break;
            case 204:
                filaMatrizProducciones = 4;
                break;
            case 205:
                filaMatrizProducciones = 5;
                break;
            case 206:
                filaMatrizProducciones = 6;
                break;
            case 207:
                filaMatrizProducciones = 7;
                break;
            case 208:
                filaMatrizProducciones = 8;
                break;
            case 211:
                filaMatrizProducciones = 9;
                break;
            case 212:
                filaMatrizProducciones = 10;
                break;
            case 209:
                filaMatrizProducciones = 11;
                break;
            case 210:
                filaMatrizProducciones = 12;
                break;
            case 370:
                filaMatrizProducciones = 13;
                break;
            case 213:
                filaMatrizProducciones = 14;
                break;
            case 214:
                filaMatrizProducciones = 15;
                break;
            case 218:
                filaMatrizProducciones = 16;
                break;
            case 219:
                filaMatrizProducciones = 17;
                break;
            case 220:
                filaMatrizProducciones = 18;
                break;
            case 221:
                filaMatrizProducciones = 19;
                break;
            case 222:
                filaMatrizProducciones = 20;
                break;
            case 215:
                filaMatrizProducciones = 21;
                break;
            case 216:
                filaMatrizProducciones = 22;
                break;
            case 217:
                filaMatrizProducciones = 23;
                break;
            case 232:
                filaMatrizProducciones = 24;
                break;
            case 233:
                filaMatrizProducciones = 25;
                break;
            case 234:
                filaMatrizProducciones = 26;
                break;
            case 235:
                filaMatrizProducciones = 27;
                break;
            case 236:
                filaMatrizProducciones = 28;
                break;
            case 237:
                filaMatrizProducciones = 29;
                break;
            case 238:
                filaMatrizProducciones = 30;
                break;
            case 239:
                filaMatrizProducciones = 31;
                break;
            case 240:
                filaMatrizProducciones = 32;
                break;
            case 241:
                filaMatrizProducciones = 33;
                break;
            case 242:
                filaMatrizProducciones = 34;
                break;
            case 243:
                filaMatrizProducciones = 35;
                break;
            case 244:
                filaMatrizProducciones = 36;
                break;
            case 245:
                filaMatrizProducciones = 37;
                break;
            case 246:
                filaMatrizProducciones = 38;
                break;
            case 247:
                filaMatrizProducciones = 39;
                break;
            case 248:
                filaMatrizProducciones = 40;
                break;
            case 249:
                filaMatrizProducciones = 41;
                break;
            case 226:
                filaMatrizProducciones = 42;
                break;
            case 227:
                filaMatrizProducciones = 43;
                break;
            case 228:
                filaMatrizProducciones = 44;
                break;
            case 229:
                filaMatrizProducciones = 45;
                break;
            case 230:
                filaMatrizProducciones = 46;
                break;
            case 231:
                filaMatrizProducciones = 47;
                break;
            case 223:
                filaMatrizProducciones = 48;
                break;
            case 224:
                filaMatrizProducciones = 49;
                break;
            case 225:
                filaMatrizProducciones = 50;
                break;
            case 250:
                filaMatrizProducciones = 51;
                break;
            case 251:
                filaMatrizProducciones = 52;
                break;
            case 252:
                filaMatrizProducciones = 53;
                break;
            case 253:
                filaMatrizProducciones = 54;
                break;
            case 254:
                filaMatrizProducciones = 55;
                break;
            case 255:
                filaMatrizProducciones = 56;
                break;
            case 260:
                filaMatrizProducciones = 57;
                break;
            case 261:
                filaMatrizProducciones = 58;
                break;
            case 262:
                filaMatrizProducciones = 59;
                break;
            case 263:
                filaMatrizProducciones = 60;
                break;
            case 264:
                filaMatrizProducciones = 61;
                break;
            case 258:
                filaMatrizProducciones = 62;
                break;
            case 259:
                filaMatrizProducciones = 63;
                break;
            case 256:
                filaMatrizProducciones = 64;
                break;
            case 257:
                filaMatrizProducciones = 65;
                break;
            case 365:
                filaMatrizProducciones = 66;
                break;
            case 366:
                filaMatrizProducciones = 67;
                break;
            case 367:
                filaMatrizProducciones = 68;
                break;
            case 368:
                filaMatrizProducciones = 69;
                break;

        }
        return filaMatrizProducciones - 1;
    }
}
