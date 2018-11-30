// moment.js language configuration
// language : spanish (es)
// author : Julio NapurÃ­ : https://github.com/julionc

(function (factory) {
    factory(moment);
}(function (moment) {
    return moment.lang('es', {
        months : "Enero_Febrero_Marzo_Abril_Mayo_Junio_Julio_Agosto_Septiembre_Octubre_Noviembre_Diciembre".split("_"),
        monthsShort : "Ene_Feb_Mar_Abr_May_Jun_Jul_Ago_Sep_Oct_Nov_Dic".split("_"),
        weekdays : "Domingo_Lunes_Martes_Mi&eacute;rcoles_Jueves_Viernes_S&aacute;bado".split("_"),
        weekdaysShort : "Dom_Lun_Mar_Mi&eacute;_Jue_Vie_S&aactue;b.".split("_"),
        weekdaysMin : "Do_Lu_Ma_Mi_Ju_Vi_S&aacute;".split("_"),
        longDateFormat : {
            LT : "H:mm",
            L : "DD/MM/YYYY",
            LL : "D [de] MMMM [de] YYYY",
            LLL : "D [de] MMMM [de] YYYY LT",
            LLLL : "dddd, D [de] MMMM [de] YYYY LT"
        },
        calendar : {
            sameDay : function () {
                return '[hoy a la' + ((this.hours() !== 1) ? 's' : '') + '] LT';
            },
            nextDay : function () {
                return '[ma&ntilde;ana a la' + ((this.hours() !== 1) ? 's' : '') + '] LT';
            },
            nextWeek : function () {
                return 'dddd [a la' + ((this.hours() !== 1) ? 's' : '') + '] LT';
            },
            lastDay : function () {
                return '[ayer a la' + ((this.hours() !== 1) ? 's' : '') + '] LT';
            },
            lastWeek : function () {
                return '[el] dddd [pasado a la' + ((this.hours() !== 1) ? 's' : '') + '] LT';
            },
            sameElse : 'L'
        },
        relativeTime : {
            future : "en %s",
            past : "hace %s",
            s : "unos segundos",
            m : "un minuto",
            mm : "%d minutos",
            h : "una hora",
            hh : "%d horas",
            d : "un d&iacute;a",
            dd : "%d d&iacute;as",
            M : "un mes",
            MM : "%d meses",
            y : "un a&ntilde;o",
            yy : "%d a&ntilde;os"
        },
        ordinal : '%d&ordm;',
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
}));