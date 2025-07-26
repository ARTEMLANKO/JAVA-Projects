import java.io.*;
import java.util.*;


public class Cache {
    //оставил много комментариев для проверяющего
    static int MEM_SIZE = 262144;
    static int CACHE_SIZE = 64 * 32;
    static int CACHE_LINE_SIZE = 64;
    static int CACHE_LINE_COUNT = 32;
    static int CACHE_WAY = 4;
    static int CACHE_SETS = 8;
    static int ADDR_LEN = 18;
    static int CACHE_TAG_LEN = 9;
    static int CACHE_INDEX_LEN = 3;
    static int CACHE_OFFSET_LEN = 6;
    // lru[i] - i-ый cache_set; lru[i][0:3 - теги кеш линий; 4:7 - времена последних обращений к каждой из них; 8 - максимальное время среди последних обращений; 9-12 - в кеш линии актуальное значение или нет (write-back);
    static int[][] lruCache = new int[CACHE_SETS][13];
    static int[][] plruCache = new int[CACHE_SETS][12];
    static int add = 4;
    //это массив с 32 регистрами, так получилось, что я назвал его instructions:(
    static int[] instructions = new int[32];
    static int ra = 0x10000 - 4;
    static int lruCnt = 0;
    static int pLruCnt = 0;
    static int AllCnt = 0;

    static int lruCntData = 0;
    static int pLruCntData = 0;
    static int AllCntData = 0;

    static int lruCntInstructions = 0;
    static int pLruCntInstructions = 0;
    static int AllCntInstructions = 0;

    private static void work_with_cash(int index, int tag, boolean inst, boolean change) {
        AllCnt++;
        boolean flag = true;

        for (int i = 0; i < 4; i++) {
            if (lruCache[index][i] == tag) {
                lruCnt++;
                if (inst) {
                    lruCntInstructions++;
                } else {
                    lruCntData++;
                }
                if (change) {
                    lruCache[index][i + 9] = 1;
                    // здесь по write-back храним в кеше актуальную информацию
                }
                lruCache[index][8]++;
                lruCache[index][i + 4] = lruCache[index][8];
                flag = false;
                break;
            }
        }

        if (flag) {
            int minTime = Integer.MAX_VALUE;
            for (int i = 0; i < 4; i++) {
                if (lruCache[index][i + 4] < minTime) {
                    minTime = lruCache[index][i + 4];
                }
            }

            for (int i = 0; i < 4; i++) {
                if (lruCache[index][i + 4] == minTime) {
                    lruCache[index][i] = tag;
                    lruCache[index][8]++;
                    lruCache[index][i + 4] = lruCache[index][8];
                    if (lruCache[index][i + 9] == 1) {
                        // по write back у нас в кеше по этому адресу актуальные данные, так что вот тут обновляем их в памяти
                    }
                    if (change) {
                        lruCache[index][i + 9] = 1;
                    } else {
                        lruCache[index][i + 9] = 0;
                    }
                    break;
                }
            }
        }

        flag = true;
        for (int i = 0; i < 4; i++) {
            if (plruCache[index][i] == tag) {
                pLruCnt++;
                plruCache[index][i + 4] = 1;
                if (inst) {
                    pLruCntInstructions++;
                } else {
                    pLruCntData++;
                }

                int zeroCount = 0;
                for (int j = 4; j < 8; j++) {
                    if (plruCache[index][j] == 0) {
                        zeroCount++;
                    }
                }
                if (zeroCount == 0) {
                    for (int j = 4; j < 8; j++) {
                        if (j != i + 4) {
                            plruCache[index][j] = 0;
                        }
                    }
                }
                flag = false;
                if (change) {
                    plruCache[index][i+8] = 1;
                    // снова write-back
                }
                break;
            }
        }

        if (flag) {
            for (int i = 4; i < 8; i++) {
                if (plruCache[index][i] == 0) {
                    plruCache[index][i - 4] = tag;
                    plruCache[index][i] = 1;

                    int zeroCount = 0;
                    for (int j = 4; j < 8; j++) {
                        if (plruCache[index][j] == 0) {
                            zeroCount++;
                        }
                    }
                    if (zeroCount == 0) {
                        for (int j = 4; j < 8; j++) {
                            if (j != i) {
                                plruCache[index][j] = 0;
                            }
                        }
                    }

                    if (plruCache[index][i+4] == 1) {
                        //write-back
                    }

                    if (change) {
                        plruCache[index][i+4] = 1;
                    } else {
                        plruCache[index][i+4] = 0;
                    }
                    break;
                }
            }
        }
    }

    //это функция для парсинга аргументов fence
    public static int newint(String a) {
        int c = 0;
        try {
            c = Integer.parseInt(a);
            return c;
        } catch (Exception e) {
            for (int sh = 0; sh < a.length (); sh++) {
                if (a.charAt(sh) == 'i') {
                    c += 8;
                } else if (a.charAt(sh) == 'o') {
                    c += 4;
                } else if (a.charAt(sh) == 'r') {
                    c += 2;
                } else if (a.charAt(sh) == 'w') {
                    c += 1;
                } else {
                    System.err.println("Incorrect fence arguments");
                    System.exit(0);
                }
            }
            return c;
        }
    }

    //в парсере пытаемся считать следующее название регистра
    public static int nextString(List<String> arguments, int i, Map<String, Integer> dict_registrs) throws Exception {
        if (dict_registrs.containsKey(arguments.get(i))) {
            return dict_registrs.get(arguments.get(i));
        } else {
            throw new Exception();
        }
    }

    //в парсере пытаемся считать следующее число
    public static int NEXTINT(List<String> arguments, int i, Map<String, Integer> dict_registrs) throws Exception {
        String next1 = arguments.get(i);
        try {
            if (next1.length() > 1 && next1.charAt(0) == '0' && next1.charAt(1) == 'x') {
                return Integer.parseInt(next1.substring(2, next1.length()), 16);
            } else if (next1.length() > 2 && next1.charAt(0) == '-' && next1.length() > 2 && next1.charAt(1) == '0' && next1.charAt(2) == 'x') {
                return -1 * Integer.parseInt(next1.substring(3, next1.length()), 16);
            } else {
                return Integer.parseInt(next1);
            }
        } catch (Exception e) {
            throw new Exception();
        }
    }

    public static void main (String[] args) {
        BufferedReader file = null;
        BufferedWriter filewritermachine = null;
        Scanner sc;
        //System.out.println(args[0]);
        try {
            String asm = null;
            String bin = null;
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("--asm") && i != args.length - 1) {
                    asm = args[i+1];
                }
                if (args[i].equals("--bin") && i != args.length - 1) {
                    bin = args[i+1];
                }
            }
            if (args.length == 4 && !(asm != null && bin != null)) {
                System.err.println("Wrong input in command line");
                System.exit(0);
            }
            if (args.length == 2 && asm == null) {
                System.err.println("Wrong input in command line");
                System.exit(0);
            }
            if (args.length != 2 && args.length != 4) {
                System.err.println("Wrong input in command line");
                System.exit(0);
            }

            file = new BufferedReader(new InputStreamReader(new FileInputStream(asm), "utf-8"), 1024);
            sc = new Scanner(file);
            List<List<Integer>> commands = new ArrayList<>();
            List<String> commands_name = new ArrayList<>();
            //разделили команды по категориям;
            List<String> categoria1 = new ArrayList<>(Arrays.asList("lui", "auipc", "jal"));
            List<String> categoria2 = new ArrayList<>(Arrays.asList("jalr", "beq", "bne", "blt", "bge", "bltu", "bgeu", "addi", "slti", "sltiu", "xori", "ori", "andi", "slli", "srli", "srai"));
            List<String> categoria3 = new ArrayList<>(Arrays.asList("add", "sub", "sll", "slt", "sltu", "xor", "srl", "sra", "or", "and", "mul", "mulh", "mulhsu", "mulhu", "div", "divu", "rem", "remu"));
            List<String> categoria4 = new ArrayList<>(Arrays.asList("lb", "lh", "lw", "lbu", "lhu", "sb", "sh", "sw"));
            Map<String, Integer> dict_registrs = new TreeMap<>();
            for (int x = 0; x < 32; x++) {
                dict_registrs.put("x" + Integer.toString(x), x);
            }
            dict_registrs.put("zero", 0);
            dict_registrs.put("ra", 1);
            dict_registrs.put("sp", 2);
            dict_registrs.put("gp", 3);
            dict_registrs.put("tp", 4);
            dict_registrs.put("t0", 5);
            dict_registrs.put("t1", 6);
            dict_registrs.put("t2", 7);
            dict_registrs.put("s0", 8);
            dict_registrs.put("fp", 8);
            dict_registrs.put("s1", 9);
            dict_registrs.put("a0", 10);
            dict_registrs.put("a1", 11);
            dict_registrs.put("a2", 12);
            dict_registrs.put("a3", 13);
            dict_registrs.put("a4", 14);
            dict_registrs.put("a5", 15);
            dict_registrs.put("a6", 16);
            dict_registrs.put("a7", 17);
            dict_registrs.put("s2", 18);
            dict_registrs.put("s3", 19);
            dict_registrs.put("s4", 20);
            dict_registrs.put("s5", 21);
            dict_registrs.put("s6", 22);
            dict_registrs.put("s7", 23);
            dict_registrs.put("s8", 24);
            dict_registrs.put("s9", 25);
            dict_registrs.put("s10", 26);
            dict_registrs.put("s11", 27);
            dict_registrs.put("t3", 28);
            dict_registrs.put("t4", 29);
            dict_registrs.put("t5", 30);
            dict_registrs.put("t6", 31);
            //тут начинается парсер
            List<String> arguments = new ArrayList<>();
            while (sc.hasNext()) {
                String next1 = sc.next();
                next1 = next1.replaceAll(","," ");
                Scanner sc3 = new Scanner(next1);
                while (sc3.hasNext()) {
                    String sssss = sc3.next();
                    arguments.add(sssss);
                }
            }
            int iii = 0;
            try {
                while (iii < arguments.size()) {
                    if (categoria1.contains(arguments.get(iii)) || categoria2.contains(arguments.get(iii)) || categoria3.contains(arguments.get(iii)) || categoria4.contains(arguments.get(iii)) || arguments.get(iii).equals("ecall") || arguments.get(iii).equals("ebreak") || arguments.get(iii).equals("fence")) {
                        String currentCommand = arguments.get(iii);
                        iii++;
                        if (currentCommand.equals("ecall") || currentCommand.equals("ebreak")) {
                            commands_name.add(currentCommand);
                            List<Integer> tek = new ArrayList<>();
                            commands.add(tek);
                            continue;
                        }
                        if (categoria1.contains(currentCommand)) {
                            int a1 = nextString(arguments, iii, dict_registrs);
                            iii++;
                            int a2 = 0;
                            try {
                                a2 = NEXTINT(arguments, iii, dict_registrs);
                            } catch (Exception e) {
                                System.err.println("Wrong Argument");
                                System.exit(0);
                            }
                            iii++;
                            commands_name.add(currentCommand);
                            List<Integer> tek = new ArrayList<>();
                            tek.add(a1);
                            tek.add(a2);
                            commands.add(tek);
                        } else if (categoria2.contains(currentCommand)) {
                            int a1 = nextString(arguments, iii, dict_registrs);
                            iii++;
                            int a2 = nextString(arguments, iii, dict_registrs);
                            iii++;
                            int a3 = 0;
                            try {
                                a3 = NEXTINT(arguments, iii, dict_registrs);
                            } catch (Exception e) {
                                System.err.println("Wrong Argument");
                                System.exit(0);
                            }
                            iii++;
                            commands_name.add(currentCommand);
                            List<Integer> tek = new ArrayList<>();
                            tek.add(a1);
                            tek.add(a2);
                            tek.add(a3);
                            commands.add(tek);
                        } else if (categoria4.contains(currentCommand)) {
                            int a1 = nextString(arguments, iii, dict_registrs);
                            iii++;
                            int a2 = 0;
                            try {
                                a2 = NEXTINT(arguments, iii, dict_registrs);
                            } catch (Exception e) {
                                System.err.println("Wrong Argument");
                                System.exit(0);
                            }
                            iii++;
                            int a3 = nextString(arguments, iii, dict_registrs);
                            iii++;
                            commands_name.add(currentCommand);
                            List<Integer> tek = new ArrayList<>();
                            tek.add(a1);
                            tek.add(a2);
                            tek.add(a3);
                            commands.add(tek);
                        } else if (categoria3.contains(currentCommand)) {
                            int a1 = nextString(arguments, iii, dict_registrs);
                            iii++;
                            int a2 = nextString(arguments, iii, dict_registrs);
                            iii++;
                            int a3 = nextString(arguments, iii, dict_registrs);
                            iii++;
                            commands_name.add(currentCommand);
                            List<Integer> tek = new ArrayList<>();
                            tek.add(a1);
                            tek.add(a2);
                            tek.add(a3);
                            commands.add(tek);
                        } else if (currentCommand.equals("fence")) {
                            List <Integer> int1 = new ArrayList<>();
                            int c = newint(arguments.get(iii));
                            int1.add(c);
                            iii++;
                            if (arguments.get(iii).equals("ecall") || arguments.get(iii).equals("ebreak")) {
                                commands_name.add(currentCommand);
                                commands.add(int1);
                            } else if (!(categoria1.contains(arguments.get(iii)) || categoria2.contains(arguments.get(iii)) || categoria3.contains(arguments.get(iii)) || categoria4.contains(arguments.get(iii)) || arguments.get(iii).equals("ecall") || arguments.get(iii).equals("ebreak")) || iii == arguments.size() - 1) {
                                commands_name.add(currentCommand);
                                if (!(arguments.get(iii).equals("fence"))) {
                                    c = newint(arguments.get(iii));
                                    int1.add(c);
                                    iii++;
                                }
                                commands.add(int1);
                            } else {
                                if (iii < arguments.size() - 1) {
                                    boolean t = true;
                                    if (!(categoria1.contains(arguments.get(iii+1)) || categoria2.contains(arguments.get(iii+1)) || categoria3.contains(arguments.get(iii+1)) || categoria4.contains(arguments.get(iii+1)) || arguments.get(iii+1).equals("ecall") || arguments.get(iii+1).equals("ebreak") || arguments.get(iii+1).equals("fence"))) {
                                        iii--;
                                        t = false;
                                    }
                                    commands_name.add(currentCommand);
                                    if (t) {
                                        c = newint(arguments.get(iii));
                                        int1.add(c);
                                    }
                                    commands.add(int1);
                                    iii++;
                                } else {
                                    commands_name.add(currentCommand);
                                    c = newint(arguments.get(iii));
                                    int1.add(c);
                                    commands.add(int1);
                                    iii++;
                                }
                            }
                        }
                    } else {
                        throw new Exception();
                    }
                }
            } catch (Exception e) {
                System.out.println("wrong input " + commands_name.get(commands_name.size() - 1));
                System.exit(0);
            }
            Map<String, String> type_commands = new TreeMap<>();
            type_commands.put("add", "R");
            type_commands.put("sub", "R");
            type_commands.put("xor", "R");
            type_commands.put("or", "R");
            type_commands.put("and", "R");
            type_commands.put("sll", "R");
            type_commands.put("srl", "R");
            type_commands.put("sra", "R");
            type_commands.put("slt", "R");
            type_commands.put("sltu", "R");
            type_commands.put("mul", "R");
            type_commands.put("mulh", "R");
            type_commands.put("mulhsu", "R");
            type_commands.put("mulhu", "R");
            type_commands.put("div", "R");
            type_commands.put("divu", "R");
            type_commands.put("rem", "R");
            type_commands.put("remu", "R");
            type_commands.put("addi", "I");
            type_commands.put("xori", "I");
            type_commands.put("ori", "I");
            type_commands.put("andi", "I");
            type_commands.put("slli", "I");
            type_commands.put("srli", "I");
            type_commands.put("srai", "I");
            type_commands.put("slti", "I");
            type_commands.put("sltiu", "I");
            type_commands.put("lb", "II");
            type_commands.put("lh", "II");
            type_commands.put("lw", "II");
            type_commands.put("lbu", "II");
            type_commands.put("lhu", "II");
            type_commands.put("sb", "S");
            type_commands.put("sh", "S");
            type_commands.put("sw", "S");
            type_commands.put("beq", "B");
            type_commands.put("bne", "B");
            type_commands.put("blt", "B");
            type_commands.put("bge", "B");
            type_commands.put("bltu", "B");
            type_commands.put("bgeu", "B");
            type_commands.put("lui", "U");
            type_commands.put("auipc", "U");
            type_commands.put("jalr", "I");
            type_commands.put("jal", "J");
            type_commands.put("ecall", "I");
            type_commands.put("ebreak", "I");
            type_commands.put("fence", "F");
            instructions[1] = 0x10000 - 4;
            int pc = 0x10000;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 4; j++) {
                    lruCache[i][j] = Integer.MAX_VALUE;
                    plruCache[i][j] = Integer.MAX_VALUE;
                }
            }

            for (int i = 0; i < commands.size(); i++) {
                if (type_commands.get(commands_name.get(i)).equals("I") && !commands_name.get(i).equals("ecall") && !commands_name.get(i).equals("ebreak")) {
                    int imm = commands.get(i).get(2);
                    imm = (imm << 20) >> 20;
                    if (commands_name.get(i).equals("srai") || commands_name.get(i).equals("srli") || commands_name.get(i).equals("slli")) {
                        imm = imm & ((1 << 5) - 1);
                        if (commands_name.get(i).equals("srai")) {
                            imm += (1 << 10);
                        }
                    }
                    commands.get(i).set(2, imm);
                } else if (type_commands.get(commands_name.get(i)).equals("II")) {
                    int imm = commands.get(i).get(1);
                    imm = (imm << 20) >> 20;
                    commands.get(i).set(1, imm);
                } else if (type_commands.get(commands_name.get(i)).equals("S")) {
                    int imm = commands.get(i).get(1);
                    imm = (imm << 20) >> 20;
                    commands.get(i).set(1, imm);
                } else if (type_commands.get(commands_name.get(i)).equals("B")) {
                    int imm = commands.get(i).get(2);
                    imm = ((imm << 19) >> 20) << 1;
                    commands.get(i).set(2, imm);
                } else if (type_commands.get(commands_name.get(i)).equals("U")) {
                    int imm = commands.get(i).get(1) << 12;
                    commands.get(i).set(1, imm);
                } else if (type_commands.get(commands_name.get(i)).equals("J")){
                    int imm = ((commands.get(i).get(1) << 11) >> 12) << 1;
                    commands.get(i).set(1, imm);
                }
            }
            while (true) {
                if (pc == ra) {
                    break;
                }
                if (!(pc >= 0x10000 && pc < 0x10000 + commands.size() * 4)) {
                    break;
                }
                int index = (pc >>> 6) & 7;
                int tag = (pc >>> 9) & 511;
                AllCntInstructions++;
                //делаем обращение в кеш для поиска инструкции
                work_with_cash(index, tag, true, false);

                int i = (pc - 0x10000) / 4;
                //исполнение команд
                if (commands_name.get(i).equals("lui")) {
                    instructions[commands.get(i).get(0)] = commands.get(i).get(1);
                } else if (commands_name.get(i).equals("auipc")) {
                    instructions[commands.get(i).get(0)] = pc + (commands.get(i).get(1));
                } else if (commands_name.get(i).equals("jal")) {
                    instructions[commands.get(i).get(0)] = pc + add;
                    pc += commands.get(i).get(1);
                    pc -= add;
                } else if (commands_name.get(i).equals("beq")) {
                    if (instructions[commands.get(i).get(0)] == instructions[commands.get(i).get(1)]) {
                        pc += commands.get(i).get(2);
                        pc -= add;
                    }
                } else if (commands_name.get(i).equals("bne")) {
                    if (instructions[commands.get(i).get(0)] != instructions[commands.get(i).get(1)]) {
                        pc += commands.get(i).get(2);
                        pc -= add;
                    }
                } else if (commands_name.get(i).equals("blt")) {
                    if (instructions[commands.get(i).get(0)] < instructions[commands.get(i).get(1)]) {
                        pc += commands.get(i).get(2);
                        pc -= add;
                    }
                } else if (commands_name.get(i).equals("bge")) {
                    if (instructions[commands.get(i).get(0)] >= instructions[commands.get(i).get(1)]) {
                        pc += commands.get(i).get(2);
                        pc -= add;
                    }
                } else if (commands_name.get(i).equals("bltu")) {
                    if (((long) instructions[commands.get(i).get(0)] & 0x00000000ffffffffL) < ((long) instructions[commands.get(i).get(1)] & 0x00000000ffffffffL)) {
                        pc += commands.get(i).get(2);
                        pc -= add;
                    }
                } else if (commands_name.get(i).equals("bgeu")) {
                    if (((long) instructions[commands.get(i).get(0)] & 0x00000000ffffffffL) >= ((long) instructions[commands.get(i).get(1)] & 0x00000000ffffffffL)) {
                        pc += commands.get(i).get(2);
                        pc -= add;
                    }
                } else if (commands_name.get(i).equals("add")) {
                    instructions[commands.get(i).get(0)] = instructions[commands.get(i).get(1)] + instructions[commands.get(i).get(2)];
                } else if (commands_name.get(i).equals("sub")) {
                    instructions[commands.get(i).get(0)] = instructions[commands.get(i).get(1)] - instructions[commands.get(i).get(2)];
                } else if (commands_name.get(i).equals("xor")) {
                    instructions[commands.get(i).get(0)] = instructions[commands.get(i).get(1)] ^ instructions[commands.get(i).get(2)];
                } else if (commands_name.get(i).equals("or")) {
                    instructions[commands.get(i).get(0)] = instructions[commands.get(i).get(1)] | instructions[commands.get(i).get(2)];
                } else if (commands_name.get(i).equals("and")) {
                    instructions[commands.get(i).get(0)] = instructions[commands.get(i).get(1)] & instructions[commands.get(i).get(2)];
                } else if (commands_name.get(i).equals("sll")) {
                    instructions[commands.get(i).get(0)] = instructions[commands.get(i).get(1)] << instructions[commands.get(i).get(2)];
                } else if (commands_name.get(i).equals("srl")) {
                    instructions[commands.get(i).get(0)] = instructions[commands.get(i).get(1)] >>> instructions[commands.get(i).get(2)];
                } else if (commands_name.get(i).equals("sra")) {
                    instructions[commands.get(i).get(0)] = instructions[commands.get(i).get(1)] >> instructions[commands.get(i).get(2)];
                } else if (commands_name.get(i).equals("slt")) {
                    instructions[commands.get(i).get(0)] = instructions[commands.get(i).get(1)] < instructions[commands.get(i).get(2)] ? 1 : 0;
                } else if (commands_name.get(i).equals("sltu")) {
                    if (((long) instructions[commands.get(i).get(1)] & 0x00000000ffffffffL) < ((long) instructions[commands.get(i).get(2)] & 0x00000000ffffffffL)) {
                        instructions[commands.get(i).get(0)] = 1;
                    } else {
                        instructions[commands.get(i).get(0)] = 0;
                    }
                } else if (commands_name.get(i).equals("mul")) {
                    instructions[commands.get(i).get(0)] = instructions[commands.get(i).get(1)] * instructions[commands.get(i).get(2)];
                } else if (commands_name.get(i).equals("mulh")) {
                    instructions[commands.get(i).get(0)] = (int) ((((long) instructions[commands.get(i).get(1)]) * ((long) instructions[commands.get(i).get(2)])) >>> 32);
                } else if (commands_name.get(i).equals("mulhsu")) {
                    instructions[commands.get(i).get(0)] = (int) ((((long) instructions[commands.get(i).get(1)]) * ((long) instructions[commands.get(i).get(2)] & 0x00000000ffffffffL)) >>> 32);
                } else if (commands_name.get(i).equals("mulhu")) {
                    instructions[commands.get(i).get(0)] = (int) ((((long) instructions[commands.get(i).get(1)] & 0x00000000ffffffffL) * ((long) instructions[commands.get(i).get(2)] & 0x00000000ffffffffL)) >>> 32);
                } else if (commands_name.get(i).equals("div")) {
                    instructions[commands.get(i).get(0)] = instructions[commands.get(i).get(1)] / instructions[commands.get(i).get(2)];
                } else if (commands_name.get(i).equals("divu")) {
                    instructions[commands.get(i).get(0)] = (int) (((((long) instructions[commands.get(i).get(1)]) & 0x00000000ffffffffL) / (((long) instructions[commands.get(i).get(2)]) & 0x00000000ffffffffL)) & 0x00000000ffffffffL);
                } else if (commands_name.get(i).equals("rem")) {
                    instructions[commands.get(i).get(0)] = instructions[commands.get(i).get(1)] % instructions[commands.get(i).get(2)];
                } else if (commands_name.get(i).equals("remu")) {
                    instructions[commands.get(i).get(0)] = (int) (((((long) instructions[commands.get(i).get(1)]) & 0x00000000ffffffffL) % (((long) instructions[commands.get(i).get(2)]) & 0x00000000ffffffffL)) & 0x00000000ffffffffL);
                } else if (commands_name.get(i).equals("addi")) {
                    instructions[commands.get(i).get(0)] = instructions[commands.get(i).get(1)] + commands.get(i).get(2);
                } else if (commands_name.get(i).equals("xori")) {
                    instructions[commands.get(i).get(0)] = instructions[commands.get(i).get(1)] ^ commands.get(i).get(2);
                } else if (commands_name.get(i).equals("ori")) {
                    instructions[commands.get(i).get(0)] = instructions[commands.get(i).get(1)] | commands.get(i).get(2);
                } else if (commands_name.get(i).equals("andi")) {
                    instructions[commands.get(i).get(0)] = instructions[commands.get(i).get(1)] & commands.get(i).get(2);
                } else if (commands_name.get(i).equals("slli")) {
                    instructions[commands.get(i).get(0)] = instructions[commands.get(i).get(1)] << (commands.get(i).get(2) & 31);
                } else if (commands_name.get(i).equals("srli")) {
                    instructions[commands.get(i).get(0)] = instructions[commands.get(i).get(1)] >>> (commands.get(i).get(2) & 31);
                } else if (commands_name.get(i).equals("srai")) {
                    instructions[commands.get(i).get(0)] = instructions[commands.get(i).get(1)] >> (commands.get(i).get(2) & 31);
                } else if (commands_name.get(i).equals("slti")) {
                    instructions[commands.get(i).get(0)] = instructions[commands.get(i).get(1)] < commands.get(i).get(2) ? 1 : 0;
                } else if (commands_name.get(i).equals("sltiu")) {
                    if (((long) instructions[commands.get(i).get(1)] & 0x00000000ffffffffL) < ((long) commands.get(i).get(2) & 0x00000000ffffffffL)) {
                        instructions[commands.get(i).get(0)] = 1;
                    } else {
                        instructions[commands.get(i).get(0)] = 0;
                    }
                } else if (commands_name.get(i).equals("ecall") || commands_name.get(i).equals("ebreak") || commands_name.get(i).equals("fence")) {
                    //:(
                } else if (commands_name.get(i).equals("jalr")) {
                    instructions[commands.get(i).get(0)] = pc + 4;
                    pc = ((instructions[commands.get(i).get(1)] + commands.get(i).get(2)) & (-2)) - 4;
                } else if (commands_name.get(i).equals("lb") || commands_name.get(i).equals("lh") || commands_name.get(i).equals("lw") || commands_name.get(i).equals("lbu") || commands_name.get(i).equals("lhu")) {
                    int addr = instructions[commands.get(i).get(2)] + commands.get(i).get(1);
                    index = (addr >>> 6) & 7;
                    tag = (addr >>> 9) & 511;
                    AllCntData++;
                    //обращение в кеш для поиска ячейки памяти с адресом addr
                    work_with_cash(index, tag, false, false);
                } else if (commands_name.get(i).equals("sb") || commands_name.get(i).equals("sh") || commands_name.get(i).equals("sw")) {
                    int addr = instructions[commands.get(i).get(2)] + commands.get(i).get(1);
                    index = (addr >>> 6) & 7;
                    tag = (addr >>> 9) & 511;
                    AllCntData++;
                    //обращение в кеш для поиска ячейки памяти с адресом addr
                    work_with_cash(index, tag, false, true);
                }
                pc += 4;
                instructions[0] = 0;
            }

            double ans1, ans2, ans3, ans4, ans5, ans6;
            if (AllCnt == 0) {
                ans1 = Double.NaN;
                ans4 = Double.NaN;
            } else {
                ans1 = (double) lruCnt / (double) AllCnt * 100;
                ans4 = (double) pLruCnt / (double) AllCnt * 100;
            }
            if (AllCntInstructions == 0) {
                ans2 = Double.NaN;
                ans5 = Double.NaN;
            } else {
                ans2 = (double) lruCntInstructions / (double) AllCntInstructions * 100;
                ans5 = (double) pLruCntInstructions / (double) AllCntInstructions * 100;
            }
            if (AllCntData == 0) {
                ans3 = Double.NaN;
                ans6 = Double.NaN;
            } else {
                ans3 = (double) lruCntData / (double) AllCntData * 100;
                ans6 = (double) pLruCntData / (double) AllCntData * 100;
            }
            System.out.println("replacement\thit rate\thit rate (inst)\t    hit rate (data)");
            System.out.println(String.format("        LRU\t%3.5f%%\t%3.5f%%\t    %3.5f%%", ans1, ans2, ans3).replaceAll("NaN", "nan"));
            System.out.println(String.format("       pLRU\t%3.5f%%\t%3.5f%%\t    %3.5f%%", ans4, ans5, ans6).replaceAll("NaN", "nan"));

            // тут перевод в  машинный код

            if (bin != null) {
                filewritermachine = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(bin, true), "utf-8"), 1024);
                Map<String, List<String>> parametrs_commands = new TreeMap<>();
                parametrs_commands.put("add", List.of("0110011", "000", "0000000"));
                parametrs_commands.put("sub", List.of("0110011", "000", "0100000"));
                parametrs_commands.put("xor", List.of("0110011", "100", "0000000"));
                parametrs_commands.put("or", List.of("0110011", "110", "0000000"));
                parametrs_commands.put("and", List.of("0110011", "111", "0000000"));
                parametrs_commands.put("sll", List.of("0110011", "001", "0000000"));
                parametrs_commands.put("srl", List.of("0110011", "101", "0000000"));
                parametrs_commands.put("sra", List.of("0110011", "101", "0100000"));
                parametrs_commands.put("slt", List.of("0110011", "010", "0000000"));
                parametrs_commands.put("sltu", List.of("0110011", "011", "0000000"));
                parametrs_commands.put("addi", List.of("0010011", "000"));
                parametrs_commands.put("xori", List.of("0010011", "100"));
                parametrs_commands.put("ori", List.of("0010011", "110"));
                parametrs_commands.put("andi", List.of("0010011", "111"));
                parametrs_commands.put("slli", List.of("0010011", "001"));
                parametrs_commands.put("srli", List.of("0010011", "101"));
                parametrs_commands.put("srai", List.of("0010011", "101"));
                parametrs_commands.put("slti", List.of("0010011", "010"));
                parametrs_commands.put("sltiu", List.of("0010011", "011"));
                parametrs_commands.put("lb", List.of("0000011", "000"));
                parametrs_commands.put("lh", List.of("0000011", "001"));
                parametrs_commands.put("lw", List.of("0000011", "010"));
                parametrs_commands.put("lbu", List.of("0000011", "100"));
                parametrs_commands.put("lhu", List.of("0000011", "101"));
                parametrs_commands.put("sb", List.of("0100011", "000"));
                parametrs_commands.put("sh", List.of("0100011", "001"));
                parametrs_commands.put("sw", List.of("0100011", "010"));
                parametrs_commands.put("beq", List.of("1100011", "000"));
                parametrs_commands.put("bne", List.of("1100011", "001"));
                parametrs_commands.put("blt", List.of("1100011", "100"));
                parametrs_commands.put("bge", List.of("1100011", "101"));
                parametrs_commands.put("bltu", List.of("1100011", "110"));
                parametrs_commands.put("bgeu", List.of("1100011", "111"));
                parametrs_commands.put("mul", List.of("0110011", "000", "0000001"));
                parametrs_commands.put("mulh", List.of("0110011", "001", "0000001"));
                parametrs_commands.put("mulhsu", List.of("0110011", "010", "0000001"));
                parametrs_commands.put("mulhu", List.of("0110011", "011", "0000001"));
                parametrs_commands.put("div", List.of("0110011", "100", "0000001"));
                parametrs_commands.put("divu", List.of("0110011", "101", "0000001"));
                parametrs_commands.put("rem", List.of("0110011", "110", "0000001"));
                parametrs_commands.put("remu", List.of("0110011", "111", "0000001"));
                parametrs_commands.put("lui", List.of("0110111"));
                parametrs_commands.put("auipc", List.of("0010111"));
                parametrs_commands.put("jal", List.of("1101111"));
                parametrs_commands.put("jalr", List.of("1100111", "000"));
                parametrs_commands.put("ecall", List.of("1110011", "000"));
                parametrs_commands.put("ebreak", List.of("1110011", "000"));

                for (int i = 0; i < commands.size(); i++) {
                    String code = "";
                    if (type_commands.get(commands_name.get(i)).equals("R")) {
                        code = parametrs_commands.get(commands_name.get(i)).get(2);
                        String code1 = Integer.toBinaryString(commands.get(i).get(2));
                        while (code1.length() < 5) {
                            code1 = "0" + code1;
                        }
                        code += code1;
                        code1 = Integer.toBinaryString(commands.get(i).get(1));
                        while (code1.length() < 5) {
                            code1 = "0" + code1;
                        }
                        code += code1;
                        code += parametrs_commands.get(commands_name.get(i)).get(1);
                        code1 = Integer.toBinaryString(commands.get(i).get(0));
                        while (code1.length() < 5) {
                            code1 = "0" + code1;
                        }
                        code += code1;
                        code += parametrs_commands.get(commands_name.get(i)).get(0);
                    } else if (type_commands.get(commands_name.get(i)).equals("I")) {
                        if (commands_name.get(i).equals("ecall")) {
                            filewritermachine.write("73 00 00 00 ecall\n");
                            continue;
                        } else if (commands_name.get(i).equals("ebreak")) {
                            filewritermachine.write("73 00 10 00 ebreak\n");
                            continue;
                        }
                        code = Integer.toBinaryString(commands.get(i).get(2) & 4095);
                        String code1 = Integer.toBinaryString(commands.get(i).get(1));
                        while (code1.length() < 5) {
                            code1 = "0" + code1;
                        }
                        code += code1;
                        code += parametrs_commands.get(commands_name.get(i)).get(1);
                        code1 = Integer.toBinaryString(commands.get(i).get(0));
                        while (code1.length() < 5) {
                            code1 = "0" + code1;
                        }
                        code += code1;
                        code += parametrs_commands.get(commands_name.get(i)).get(0);
                    } else if (type_commands.get(commands_name.get(i)).equals("II")) {
                        code = Integer.toBinaryString(commands.get(i).get(1) & 4095);
                        String code1 = Integer.toBinaryString(commands.get(i).get(2));
                        while (code1.length() < 5) {
                            code1 = "0" + code1;
                        }
                        code += code1;
                        code += parametrs_commands.get(commands_name.get(i)).get(1);
                        code1 = Integer.toBinaryString(commands.get(i).get(0));
                        while (code1.length() < 5) {
                            code1 = "0" + code1;
                        }
                        code += code1;
                        code += parametrs_commands.get(commands_name.get(i)).get(0);
                    } else if (type_commands.get(commands_name.get(i)).equals("S")) {
                        code = Integer.toBinaryString((commands.get(i).get(1) & 4064) >>> 5);
                        String code1 = Integer.toBinaryString(commands.get(i).get(0));
                        while (code1.length() < 5) {
                            code1 = "0" + code1;
                        }
                        code += code1;
                        code1 = Integer.toBinaryString(commands.get(i).get(2));
                        while (code1.length() < 5) {
                            code1 = "0" + code1;
                        }
                        code += code1;
                        code += parametrs_commands.get(commands_name.get(i)).get(1);
                        code1 = Integer.toBinaryString(commands.get(i).get(1) & 31);
                        while (code1.length() < 5) {
                            code1 = "0" + code1;
                        }
                        code += code1;
                        code += parametrs_commands.get(commands_name.get(i)).get(0);
                    } else if (type_commands.get(commands_name.get(i)).equals("B")) {
                        code = Integer.toBinaryString((commands.get(i).get(2) & 4096) >>> 12);
                        String code1 = Integer.toBinaryString((commands.get(i).get(2) & 2016) >>> 5);
                        while (code1.length() < 6) {
                            code1 = "0" + code1;
                        }
                        code += code1;
                        code1 = Integer.toBinaryString(commands.get(i).get(1));
                        while (code1.length() < 5) {
                            code1 = "0" + code1;
                        }
                        code += code1;
                        code1 = Integer.toBinaryString(commands.get(i).get(0));
                        while (code1.length() < 5) {
                            code1 = "0" + code1;
                        }
                        code += code1;
                        code += parametrs_commands.get(commands_name.get(i)).get(1);
                        code1 = Integer.toBinaryString((commands.get(i).get(2) & 30) >>> 1);
                        while (code1.length() < 4) {
                            code1 = "0" + code1;
                        }
                        code += code1;
                        code += Integer.toBinaryString((commands.get(i).get(2) & 2048) >>> 11);
                        code += parametrs_commands.get(commands_name.get(i)).get(0);
                    } else if (type_commands.get(commands_name.get(i)).equals("U")) {
                        for (int ttt = 31; ttt >= 12; ttt--) {
                            code += Integer.toBinaryString((commands.get(i).get(1) >>> ttt) & 1);
                        }
                        String code1 = Integer.toBinaryString(commands.get(i).get(0));
                        while (code1.length() < 5) {
                            code1 = "0" + code1;
                        }
                        code += code1;
                        code += parametrs_commands.get(commands_name.get(i)).get(0);
                    } else if (type_commands.get(commands_name.get(i)).equals("J")) {
                        code = Integer.toBinaryString((commands.get(i).get(1) & (1 << 20)) >>> 20);
                        String code1 = Integer.toBinaryString((2046 & commands.get(i).get(1)) >>> 1);
                        while (code1.length() < 10) {
                            code1 = "0" + code1;
                        }
                        code += code1;
                        code += Integer.toBinaryString((2048 & commands.get(i).get(1)) >>> 11);
                        code1 = Integer.toBinaryString((((1 << 20) - 1 - ((1 << 12) - 1)) & commands.get(i).get(1)) >>> 12);
                        while (code1.length() < 8) {
                            code1 = "0" + code1;
                        }
                        code += code1;
                        code1 = Integer.toBinaryString(commands.get(i).get(0));
                        while (code1.length() < 5) {
                            code1 = "0" + code1;
                        }
                        code += code1;
                        code += parametrs_commands.get(commands_name.get(i)).get(0);
                    } else if (type_commands.get(commands_name.get(i)).equals("F")) {
                        code = "0000";
                        String code1 = Integer.toBinaryString(commands.get(i).get(0));
                        while (code1.length() < 4) {
                            code1 = "0" + code1;
                        }
                        code += code1;
                        if (commands.get(i).size() == 1) {
                            code1 = "1111";
                        } else {
                            code1 = Integer.toBinaryString(commands.get(i).get(1));
                            while (code1.length() < 4) {
                                code1 = "0" + code1;
                            }
                        }
                        code += code1;
                        code += "00000000000000001111";
                    }
                    while (code.length() < 32) {
                        code = "0" + code;
                    }
                    String[] ans = new String[8];
                    for (int ii = 0; ii < 8; ii++) {
                        String ssss = String.valueOf(code.charAt(4 * ii)) + String.valueOf(code.charAt(4 * ii + 1)) + String.valueOf(code.charAt(4 * ii + 2)) + String.valueOf(code.charAt(4 * ii + 3));
                        ans[ii] = Integer.toHexString(Integer.parseInt(ssss, 2));
                    }
                    filewritermachine.write(ans[6] + ans[7] + ' ');
                    filewritermachine.write(ans[4] + ans[5] + ' ');
                    filewritermachine.write(ans[2] + ans[3] + ' ');
                    filewritermachine.write(ans[0] + ans[1]);
                    filewritermachine.write("\n");
                }
                filewritermachine.close();
            }
        } catch (Exception e) {
            //:(
            System.out.println("RuntimeError");
        } finally {
            try {
                file.close();
            } catch (Exception e) {
                //System.out.println("unlu");
            }
        }
    }
}