struct MsgHeader
{
int sequence;
int messageLen ;
int opCode ;
toString: sequence+messageLen+opCode
} ;

struct MsgReply
{
MsgHeader header ;
int returnCode ;
int numReturn ;
char data[0] ;
} ;




todo
1,close ,发送quit，退出客户端
2，针对增删改查4中场景的client和server端
2，加入bson




struct MsgInsert
{
MsgHeader header ;
int numInsert ;
char data[0] ;
} ;
