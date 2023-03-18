import express from 'express';
import {BingChat} from 'bing-chat'
import dotenv from 'dotenv-safe'


dotenv.config()
const app = express();
app.use(express.json());
const port = 3000;


process.on('uncaughtException', (err) => {
    console.error(err);
});


app.post('/bing', async (req, res) => {
    const {param} = req.body;
    const api = new BingChat({cookie: process.env.BING_COOKIE});

    if (!param) {
        res.status(400).send('参数不能为空');
    } else {
        let isTimeout = false;
        let data = "";
        const timeout = setTimeout(() => {
            isTimeout = true;
            if (data.length > 0) {
                res.json(data);
            } else {
                res.json('使用过于频繁 已被微软暂时禁止');
            }

        }, 40000);

        const rest = await api.sendMessage(param, {
            onProgress: (partialResponse) => {
                data = partialResponse.text
                console.log(partialResponse.text)
            }
        });

        if (!isTimeout) {
            clearTimeout(timeout);
            res.json(rest.text);
        }
        res.json(rest.text);
    }
});

app.listen(port, () => {
    console.log(`服务已启动 http://localhost:${port}`);
});