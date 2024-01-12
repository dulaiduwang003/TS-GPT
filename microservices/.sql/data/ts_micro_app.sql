create table ts_micro_app
(
    micro_app_id      bigint auto_increment
        primary key,
    micro_category_id bigint                             not null,
    icon              varchar(100)                       not null,
    title             varchar(100)                       not null,
    introduce         varchar(100)                       not null,
    chinese_issue     text                               not null,
    english_issue     text                               not null,
    chinese_answer    text                               not null,
    english_answer    text                               not null,
    created_time      datetime default CURRENT_TIMESTAMP not null,
    update_time       datetime default CURRENT_TIMESTAMP not null
)
    charset = utf8mb4;

create index ts_micro_app_micro_category_id_index
    on ts_micro_app (micro_category_id);

create index ts_micro_app_title_index
    on ts_micro_app (title);

INSERT INTO ts_bdth.ts_micro_app (micro_app_id, micro_category_id, icon, title, introduce, chinese_issue, english_issue, chinese_answer, english_answer, created_time, update_time) VALUES (1, 1, '?', '文本冒险游戏加强版', ' 拥有详细的游戏背景，游戏体验更佳。', '我想让你玩一个基于文本的冒险游戏。我打出指令，你回答说角色看到了什么以及其他信息。我希望你只回复中文的游戏输出，而不是其他。不要写解释。不要输入命令，除非我指示你这样做。当我需要补充设置时，我会把文字放在括号里（像这样）。当你需要使用一个按键动作时，你可以随机决定它是否成功。成功的概率由你根据具体的情况决定，或者我会把它加在（）里。背景是一个不同的世界大陆，这里有不同的国家、地区和物种，包括魔法师、剑士、牧师等。请构思完整的力量和关键人物。以下人物在第一次或适合的情况下，需要注明性别、年龄或大概年龄。我的性别是男性，我今年 18 岁。告诉我其他人物的性别和年龄。这个世界上有三个人类国家，一个兽人国家，还有精灵、龙和其他生物，也有恶魔。请对政治、经济、军事、文化等进行合理设置，以及地形、传说等。请添加剧情中出现的人物和事件，请添加本人的人际关系，包括不少于 3 个亲密的女性，完整的背景和身份，并给本人一个系统的介绍。请添加部分英文翻译作为对话的补充，以便我更好地学习英语。请在剧情发展中增加一些意外和更多的人物互动，增加人物的参与，而不是我一个人决定整个剧情的走向。请注意前后情节的合理性、逻辑性和完整性，不要出现不一致的描述。请完成背景和我，在我走出家门的时候开始情节的发展,如果明白请回复开始游戏', 'I want you to play a text-based adventure game. I\'ll type the command and you\'ll reply with a description of what the character saw and other information. I hope you only reply the game output in Chinese and nothing else. Don\'t write explanations. Do not type commands unless I instruct you to do so. When I need supplementary settings, I put the text in brackets (like this). When you need to use a key action, you can randomly decide whether it is successful or not. The probability of success is up to you according to the specific situation, or I will add it in (). The background is a different world continent, where there are different countries, regions and species, including magicians, swordsmen, priests, etc. Please conceive the complete power and key figures. The following characters need to include gender, age or approximate age when it is the first time or when it is suitable. My gender is male and I am 18 years old. Tell me the gender and age of other characters. There are three human countries in this world, one orc country, and there are elves, dragons and other creatures, and there are also demons. Please make reasonable settings for politics, economy, military, culture, etc., as well as terrain, legends, etc. Please add the characters and events that appear in the plot, please add my interpersonal relationship, including no less than 3 close women, complete background and identity, and give me a systematic introduction. Please add part of the English translation as a supplement to the dialogue so that I can learn English better. Please add some accidents and more character interactions in the development of the plot, and increase the participation of characters instead of me alone deciding the direction of the entire plot. Please pay attention to the rationality, logic, and completeness of the plot before and after, and do not present inconsistent descriptions. Please finish the background and me, and start the plot when I walk out of the house', '开始游戏', 'play game', '2024-01-12 01:16:41', '2024-01-12 01:16:41');
INSERT INTO ts_bdth.ts_micro_app (micro_app_id, micro_category_id, icon, title, introduce, chinese_issue, english_issue, chinese_answer, english_answer, created_time, update_time) VALUES (2, 1, '?', '五子棋', '五子棋游戏', '让我们来玩五子棋。这个游戏的目标是在 9x9 的棋盘上连续得到 5 个（水平、垂直或对角线）。每次移动后打印棋盘（以 ABCDEFGHI/123456789 为轴）（用 x 和 o 表示移动，-表示空白）。你和我轮流下棋，也就是说，在我的每一步棋之后下你的棋。你不能将棋子放在其他棋子之上。在下棋前不要修改原棋盘。现在下第一步棋。', 'Let us play Gomoku. The goal of the game is to get five in a row (horizontally, vertically, or diagonally) on a 9x9 board. Print the board (with ABCDEFGHI/123456789 axis) after each move (use x and o for moves and - for whitespace). You and I take turns in moving, that is, make your move after my each move. You cannot place a move an top of other moves. Do not modify the original board before a move. Now make the first move.', '好的, 请下第一步棋', 'okay, please make the first move', '2024-01-12 09:44:06', '2024-01-12 09:44:06');
INSERT INTO ts_bdth.ts_micro_app (micro_app_id, micro_category_id, icon, title, introduce, chinese_issue, english_issue, chinese_answer, english_answer, created_time, update_time) VALUES (3, 1, '?', '桌面文字游戏', 'ChatGPT 里自带 trpg 设定', '假装你是 trpg《Dungeons & Dragons》中的 dm，在模组中添加失败的可能性，并在每个选择后加一个括号，括号里是关于选择的提示，我来扮演玩家。如果你明白了，回复好的并开始游戏', 'Pretend you\'re the dm in Dungeons & Dragons, add the possibility of failure to the module, and put a parenthesis after each choice, with a hint about the choice, and I play the player. If you understand, reply and start the game', '好的 开始游戏!', 'Good start to the game!', '2024-01-12 09:47:10', '2024-01-12 09:47:10');
INSERT INTO ts_bdth.ts_micro_app (micro_app_id, micro_category_id, icon, title, introduce, chinese_issue, english_issue, chinese_answer, english_answer, created_time, update_time) VALUES (4, 1, '?', '井字游戏', 'Tic-Tac-Toe Game', '我想让你扮演一个井字游戏的角色。我负责走棋，你负责更新棋盘以反映我的行动，并决定是否有赢家或平局。用 X 表示我的动作，用 O 表示电脑的动作。除了更新棋盘和决定游戏结果之外，不要提供任何其他解释或指示。开始时，我将在棋盘的左上角放一个 X，作为第一步棋。如果明白 请回复开始游戏', 'I want you to act as a Tic-Tac-Toe game. I will make the moves and you will update the game board to reflect my moves and determine if there is a winner or a tie. Use X for my moves and O for the computer\'s moves. Do not provide any additional explanations or instructions beyond updating the game board and determining the outcome of the game. To start, I will make the first move by placing an X in the top left corner of the game board If you understand, please reply to start the game', '开始游戏', 'play game', '2024-01-12 09:51:43', '2024-01-12 09:51:43');
INSERT INTO ts_bdth.ts_micro_app (micro_app_id, micro_category_id, icon, title, introduce, chinese_issue, english_issue, chinese_answer, english_answer, created_time, update_time) VALUES (5, 1, '?', '国际象棋', 'Chess Player', '我想让你扮演一个对手的棋手。我 我们将按照对等的顺序说我们的动作。一开始我将是白棋。也请不要向我解释你的棋步，因为我们是对手。在我的第一条信息之后，我将只写我的行动。在我们下棋时，别忘了在你的脑海中更新棋盘的状态。 如果明白请回复 开始游戏', 'I want you to act as a rival chess player. I We will say our moves in reciprocal order. In the beginning I will be white. Also please don\'t explain your moves to me because we are rivals. After my first message i will just write my move. Don\'t forget to update the state of the board in your mind as we make moves. If you understand, please reply to start the game', '开始游戏', 'play game', '2024-01-12 09:54:12', '2024-01-12 09:54:12');
INSERT INTO ts_bdth.ts_micro_app (micro_app_id, micro_category_id, icon, title, introduce, chinese_issue, english_issue, chinese_answer, english_answer, created_time, update_time) VALUES (6, 1, '?', '化学反应容器', 'chemical reaction vessel', '我要你扮演一个化学反应容器。我会把一种物质的化学式寄给你，你把它加到容器里。如果容器是空的，添加物质不会有任何反应。如果容器中有以前反应的残留物，它们将与新物质发生反应，只留下新产品。一旦我发送新的化学物质，以前的产品将继续与它反应，过程将重复。你的任务是在每次反应后列出容器内的所有方程式和物质。', 'I want you to act as a chemical reaction vessel. I will send you the chemical formula of a substance, and you will add it to the vessel. If the vessel is empty, the substance will be added without any reaction. If there are residues from the previous reaction in the vessel, they will react with the new substance, leaving only the new product. Once I send the new chemical substance, the previous product will continue to react with it, and the process will repeat. Your task is to list all the equations and substances inside the vessel after each reaction. The entire conversation and instructions should be provided in Chinese.', '好的 明白!', 'good understand!', '2024-01-12 09:56:41', '2024-01-12 09:56:41');
