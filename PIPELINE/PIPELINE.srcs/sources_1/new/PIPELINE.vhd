library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.numeric_std.ALL;


entity TESTENV is
  Port (
  btn: in std_logic_vector(4 downto 0);
  sw: in std_logic_vector(15 downto 0);
  clk: in std_logic;
  led: out std_logic_vector( 15 downto 0);
  cat: out std_logic_vector( 6 downto 0);
  an: out std_logic_vector( 3 downto 0)
  );
end TESTENV;

architecture Behavioral of TESTENV is
signal en: std_logic;
signal PC4: std_logic_vector(31 downto 0);
signal Instruction: std_logic_vector( 31 downto 0);
signal RD1: std_logic_vector( 31 downto 0);
signal RD2: std_logic_vector( 31 downto 0);
signal ExtImm: std_logic_vector( 31 downto 0);
signal func: std_logic_vector(5 downto 0);
signal sa: std_logic_vector( 4 downto 0);
signal RegDst: std_logic;
signal ExtOp: std_logic; 
signal ALUSrc: std_logic;
signal Branch: std_logic;
signal Jump: std_logic;
signal MemWrite: std_logic;
signal MemtoReg: std_logic;
signal RegWrite: std_logic;
signal ALUOp: std_logic_vector( 1 downto 0);
signal outMux8: std_logic_vector( 31 downto 0);
signal AluRes: std_logic_vector(31 downto 0);
signal MemData: std_logic_vector( 31 downto 0);
signal WD: std_logic_vector(31 downto 0);
signal SW75: std_logic_vector( 2 downto 0);
signal Instruction4: std_logic_vector(3 downto 0);
signal JumpAddress: std_logic_vector( 31 downto 0);
signal Instruction2: std_logic_vector( 27 downto 0);
signal PCSrc: std_logic;
signal Zero: std_logic;
signal BranchAddress: std_logic_vector( 31 downto 0);
signal InstrUC: std_logic_vector( 5 downto 0);
signal ALUResOut: std_logic_vector( 31 downto 0);
signal InstrID: std_logic_vector( 25 downto 0);
signal output : std_logic_vector(15 downto 0);
signal InstrIFID: std_logic_vector( 31 downto 0);
signal PC4IDIF: std_logic_vector(31 downto 0);
signal PCSRCIF: std_logic;



signal RegDstIDEX: std_logic;
signal BranchIDEX: std_logic;
signal RegWriteIDEX: std_logic;
signal RD1IDEX: std_logic_vector( 31 downto 0);
signal RD2IDEX: std_logic_vector( 31 downto 0);
signal ExtImmIDEX: std_logic_vector( 31 downto 0);
signal funcIDEX: std_logic_vector( 5 downto 0);
signal saIDEX: std_logic_vector( 4 downto 0);
signal rtIDEX: std_logic_vector ( 4 downto 0);
signal rdIDEX: std_logic_vector( 4 downto 0);
signal PC4IDEX: std_logic_vector( 31 downto 0);
signal ALUSrcIDEX: std_logic;
signal ALUOpIDEX: std_logic_vector( 1 downto 0);
signal MemWriteIDEX: std_logic;
signal MemtoRegIDEX: std_logic;


signal BranchEXMEM: std_logic;
signal MemWriteEXMEM: std_logic;
signal MemtoRegEXMEM: std_logic;
signal RegWriteEXMEM: std_logic;
signal ZeroEXMEM: std_logic;
signal BrAddrEXMEM: std_logic_vector( 31 downto 0);
signal ALUResEXMEM: std_logic_vector( 31 downto 0);
signal WAEXMEM: STD_LOGIC_VECTOR( 4 DOWNTO 0);
SIGNAL rd2EXMEM: std_logic_vector( 31 downto 0);
signal JUMPUC: std_logic;
signal pc31: std_logic_vector(3 downto 0);
signal JumpAdrressIF: std_logic_vector(31 downto 0);


signal memtoRegMEMWB: std_logic;
signal ALUResMEMWB: std_logic_vector (31 downto 0);
signal MemDataMEMWB: std_logic_vector( 31 downto 0);
signal WAMEMWB: std_logic_vector( 4 downto 0);
signal RegWriteMEMWB: std_logic;
signal rt: std_logic_vector( 4 downto 0);
signal rd: std_logic_vector( 4 downto 0);
signal rwa: std_logic_vector( 4 downto 0);



component IDvan is 
    Port (
            clk : in STD_LOGIC;
           en : in STD_LOGIC;    
           Instr : in STD_LOGIC_VECTOR(25 downto 0);
           Wd : in STD_LOGIC_VECTOR(31 downto 0);
           RegWrite : in STD_LOGIC;
           ExtOp : in STD_LOGIC;
           RD1 : out STD_LOGIC_VECTOR(31 downto 0);
           RD2 : out STD_LOGIC_VECTOR(31 downto 0);
           Ext_Imm : out STD_LOGIC_VECTOR(31 downto 0);
           func : out STD_LOGIC_VECTOR(5 downto 0);
           sa : out STD_LOGIC_VECTOR(4 downto 0);
           rd: out std_logic_vector(4 downto 0);
           rt: out std_logic_vector( 4 downto 0);
           Wa: in std_logic_vector( 4 downto 0)
    );
    end component;
    
    component MPG is 
    Port (enable : out STD_LOGIC;
           btn : in STD_LOGIC;
           clk : in STD_LOGIC);
end component;
    

component SSD is
Port (  clk : in STD_LOGIC;
           digits : in STD_LOGIC_VECTOR(15 downto 0);
           an : out STD_LOGIC_VECTOR(3 downto 0);
           cat : out STD_LOGIC_VECTOR(6 downto 0));
end component;

    
    component EXvan is
    Port(PCp4 : in STD_LOGIC_VECTOR(31 downto 0);
           RD1 : in STD_LOGIC_VECTOR(31 downto 0);
           RD2 : in STD_LOGIC_VECTOR(31 downto 0);
           Ext_Imm : in STD_LOGIC_VECTOR(31 downto 0);
           func : in STD_LOGIC_VECTOR(5 downto 0);
           sa : in STD_LOGIC_VECTOR(4 downto 0);
           ALUSrc : in STD_LOGIC;
           ALUOp : in STD_LOGIC_VECTOR(1 downto 0);
           BranchAddress : out STD_LOGIC_VECTOR(31 downto 0);
           ALURes : out STD_LOGIC_VECTOR(31 downto 0);
           rWa: out std_logic_vector( 4 downto 0);
           rt: in std_logic_vector( 4 downto 0);
           rd: in std_logic_vector( 4 downto 0);
           RegDst: in std_logic;
           Zero : out STD_LOGIC);
end component;

component MEM is
Port  ( 
    MemWrite : in std_logic;
    ALUResin : in std_logic_vector(31 downto 0);
    RD2      : in std_logic_vector(31 downto 0);
    Clk      : in std_logic;
    MemData  : out std_logic_vector(31 downto 0);
    ALUResout: out std_logic_vector(31 downto 0);
    En       : in std_logic
  );
end component;

component UC is 
Port (
    Instr: in std_logic_vector( 5 downto 0);
    RegDst: out std_logic;
    ExtOp: out std_logic;
    ALUSrc: out std_logic;
    Branch: out std_logic;
    Jump: out std_logic;
    ALUOp: out std_logic_vector( 1 downto 0);
    MemWrite: out std_logic;
    MemtoReg: out std_logic;
    RegWrite: out std_logic
 );
end component;
 component IFetchvan is
 Port (clk : in STD_LOGIC;
          rst : in STD_LOGIC;
          en : in STD_LOGIC;
          BranchAddress : in STD_LOGIC_VECTOR(31 downto 0);
          JumpAddress : in STD_LOGIC_VECTOR(31 downto 0);
          Jump : in STD_LOGIC;
          PCSrc : in STD_LOGIC;
          Instruction : out STD_LOGIC_VECTOR(31 downto 0);
          PCp4 : out STD_LOGIC_VECTOR(31 downto 0));
end component;

begin
 SW75<=SW(7 downto 5);
 pc31<=PC4IDIF( 31 downto 28);
 Instruction2<= std_logic_vector(unsigned(InstrIFID(25 downto 0)) sll 2)&"00";
 PCSrc<= Branch and Zero;
 JumpAddress<=Instruction4 & Instruction2;
 InstrUc<=Instruction(31 downto 26);
 InstrId<=Instruction( 25 downto 0);
 
 MUX: process( SW75,Instruction, PC4, RD1,RD2, ExtImm, ALURes,MemData,WD)
 begin
 case SW75 is 
 when "000"=>outMux8<=Instruction;
 when "001"=>outMux8<=PC4;
 when "010"=>outMux8<=RD1;
 when "011"=>outMux8<=RD2;
 when "100"=>outMux8<=ExtImm;
 when "101" => outMux8<=ALURes;
 when"110"=>outMux8<=MemData;
 when "111"=>outMux8<=WD;
  when others=>  outMux8<=(others => 'X');
 
 end case;
 end process MUX;
 
  process(sw(0))
begin
    if sw(0) = '0' then
        output<=outMux8(15 downto 0);
    else
        output<=outMux8(31 downto 16);
    end if;
end process;

 MUX2: process( ALUResOut, MemData, MemtoReg)
 begin 
 if MemtoReg='0' then
 WD<=ALUResOut;
 else
 WD<= MemData;
 end if;
 end process MUX2;
 
 
 IFID: process(clk, en,Instruction, PC4)
 begin
 if rising_edge(clk) then
 if en='1' then 
 instrIFID<=Instruction;
 PC4IDIF<=PC4;
 
 
  end if;
  end if;
 end process;
 
 
 IDEX: process( clk, ALUSrc, ALUOp,en,RegDst,Branch,MemWrite,MemtoReg, PC4IDIF,RegWrite, RD1,RD2,ExtImm, func,sa, rt, rd)
 begin
  if rising_edge(clk) then
 if en='1' then 
 RegDstIDEX<=RegDst;
 BranchIDEX<=Branch;
 RegWriteIDEX<=RegWrite;
 Rd1IDEX<=RD1;
 RD2IDEX<=RD2;
 ExtImmIDEX<=ExtImm;
 MemWriteIDEX<=MemWrite;
 ALUSrcIDEX<=ALUSrc;
 MemtoRegIDEX<=MemtoReg;
 funcIDEX<=func;
 ALUOpIDEX<=ALUOp;
 saIDEX<=sa;
 rdIDEX<=rd;
 rtIDEX<=rt;
 PC4IDEX<= PC4IDIF;
 
 
 end if;
 end if;
 end process;
 
 EXEM: process( BranchIDEX,MemWriteIDEX,MemtoRegIDEX,RegWriteIDEX,Zero,BranchAddress,ALURes,rWA,RD2IDEX, clk,en)
 begin 
  if rising_edge(clk) then
 if en='1' then 
 BranchEXMEM<=BranchIDEX;
 MemWriteEXMEM<=MemWriteIDEX;
 MemtoRegEXMEM<=MemtoRegIDEX;
 RegWriteEXMEM<=RegWriteIDEX;
 ZeroEXMEM<=Zero;
 BrAddrEXMEM<=BranchAddress;
 ALuResEXMEM<=ALURes;
 WAEXMEM<=rWA;
 RD2EXMEM<=RD2IDEX;
 end if;
 end if;
 
 
 
 
 end process EXEM ;
 
 MEMWB: process( MemtoRegEXMEM, RegWriteEXMEM, ALURes, MemData, WAEXMEM)
 begin 
  if rising_edge(clk) then
 if en='1' then 
 MemtoRegMEMWB<=MemtoRegEXMEM;
 RegWriteMEMWB<=RegWriteEXMEM;
 ALUResMEMWB<=ALURes;
 MemDataMEMWB<=Memdata;
 WAMEMWB<=WAEXMEM;
 end if;
 end if;
 
 
end process MEMWB;
JumpUC<=JUMP;
PCSRCIF<= BranchEXMEM and ZeroEXMEM;
JumpAdrressIF<=pc31 & instruction2;
 
 
 
 
 
monopulse :  MPG port map(en,btn(0), clk);
IFetch: IFetchvan  port map(clk,btn(1),en,BrAddrEXMEM,JumpAdrressIF,JUMPUC, PCSRCIF, Instruction,PC4);
UnitdeContr: UC port map (InstrUc,RegDst,ExtOp,ALUSrc,Branch,Jump,ALUOp,MemWrite,MemtoReg,RegWrite);
ID: IDvan port map(clk, en, instrID, WD,RegWrite,ExtOp,RD1,RD2,ExtImm,func,sa, rd, rt, waMEMWB);
Ex: EXvan port map( PC4IDEX,RD1IDEX, RD2IDEX, ExtImmIDEX,funcIDEX,saIDEX,ALUSrcIDEX,ALUOpIDEX,BranchAddress,ALURes,rwa,rtIDEX,rdIDEX,RegDstIDEX,Zero);
dispaly:SSD port map( clk,output,An,Cat);
memorie: MEM port map ( MemWrite,ALURes,RD2,clk,MemData,ALUResOut, en);
end Behavioral;
