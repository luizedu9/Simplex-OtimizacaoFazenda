var x1 >= 0;
var x2 >= 0;
var x3 >= 0;
var x4 >= 0;
var x5 >= 0;
var x6 >= 0; 
var x7 >= 0;
var x8 >= 0;
var x9 >= 0;
var x10 >= 0;
var x11 >= 0;
var x12 >= 0;
var x13 >= 0; 
var x14 >= 0;
var x15 >= 0;
var y1 >= 0;
var y2 >= 0;
var y3 >= 0;

minimize Lucro : -1.05 * x1 - 19.183 * x2 - 2.04 * x3 - 23.983 * x4 - 13.6 * x5 - 8.13 * x6 - 17.82 * x7 + 0 * x8 + 0 * x9 + 0 * x10 + 0 * x11 + 0 * x12 + 0 * x13+ 0 * x14 + 0 * x15 + 1000 * y1 + 1000 * y2 + 1000 * y3;

subject to

producaoleite: x1 + 9 * x2 + x3 + 10 * x4 + 4 * x5 + 2 * x6 + 10 * x7 + y1 = 10944;
demandaleite: x1 - x8 + y2 = 5472;
demandaqueijo : x2 - x9 + y3 = 120;
limitequeijo : x2 + x10 = 430;
limitemucarela : x3 + x11 = 30;
limiteiogurte: x4 + x12 = 70;
limitedoceleite: x5 + x13 = 20;
limiteleitecondensado: x6 + x14 = 30;
limiterequeijao: x7 + x15 = 25;

solve;

display Lucro, x1, x2, x3, x4, x5, x6, x7;

end;
