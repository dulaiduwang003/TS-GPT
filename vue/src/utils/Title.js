let titleTime, OriginTitile = document.title;
document.addEventListener("visibilitychange", (function () {
    document.hidden ? (
        document.title = "TS-GPT", clearTimeout(titleTime)) : (document.title = "欢迎回来",
            titleTime = setTimeout((function () {
                document.title = OriginTitile
            }), 2e3)
    )
}));
