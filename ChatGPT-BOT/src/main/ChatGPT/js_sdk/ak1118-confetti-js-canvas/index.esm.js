/******************************************************************************
Copyright (c) Microsoft Corporation.

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH
REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY
AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM
LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR
OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
PERFORMANCE OF THIS SOFTWARE.
***************************************************************************** */

function __awaiter(thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
}

/*
    使用代理模式重写Painter，兼容原生Painter
*/
class Painter {
    constructor(paint) {
        this.paint = null;
        this.paint = paint;
    }
    set fillStyle(style) {
        this.paint.fillStyle = style;
    }
    set lineWidth(width) {
        this.paint.lineWidth = width;
    }
    set strokeStyle(style) {
        this.paint.strokeStyle = style;
    }
    draw() {
        var _a;
        (_a = this.paint) === null || _a === void 0 ? void 0 : _a.draw();
    }
    strokeRect(x, y, w, h) {
        this.paint.strokeRect(x, y, w, h);
    }
    fillRect(x, y, w, h) {
        this.paint.fillRect(x, y, w, h);
    }
    stroke() {
        this.paint.stroke();
    }
    clearRect(x, y, w, h) {
        if (typeof (uni) != 'undefined')
            this.draw();
        else
            this.paint.clearRect(x, y, w, h);
    }
    save() {
        this.paint.save();
    }
    rotate(angle) {
        this.paint.rotate(angle);
    }
    beginPath() {
        this.paint.beginPath();
    }
    closePath() {
        this.paint.closePath();
    }
    restore() {
        this.paint.restore();
    }
    translate(x, y) {
        this.paint.translate(x, y);
    }
    fill() {
        this.paint.fill();
    }
    arc(x, y, radius, start, end) {
        this.paint.arc(x, y, radius, start, end);
    }
    scale(a, b) {
        this.paint.scale(a, b);
    }
    moveTo(x, y) {
        this.paint.moveTo(x, y);
    }
    lineTo(x, y) {
        this.paint.lineTo(x, y);
    }
    fillText(text, x, y, maxWidth) {
        this.paint.fillText(text, x, y, maxWidth);
    }
    /*清空画布|刷新画布*/
    update() {
    }
}

class FpsUtil {
    constructor() {
        this.sampleSize = 60;
        this.value = 0;
        this._sample_ = [];
        this._index_ = 0;
        this._lastTick_ = 0;
    }
    tick() {
        let _Date = Date;
        if (typeof (performance) != "undefined")
            _Date = performance;
        // if is first tick, just set tick timestamp and return
        if (!this._lastTick_) {
            this._lastTick_ = _Date.now();
            return 0;
        }
        // calculate necessary values to obtain current tick FPS
        let now = _Date.now();
        let delta = (now - this._lastTick_) * 0.001;
        let fps = 1 / delta;
        // add to fps samples, current tick fps value 
        this._sample_[this._index_] = fps >> 0;
        // iterate samples to obtain the average
        let average = 0;
        for (let i = 0; i < this._sample_.length; i++)
            average += this._sample_[i];
        average = Math.round(average / this._sample_.length);
        // set new FPS
        this.value = average;
        // store current timestamp
        this._lastTick_ = now;
        // increase sample index counter, and reset it
        // to 0 if exceded maximum sampleSize limit
        this._index_++;
        if (this._index_ === this.sampleSize)
            this._index_ = 0;
        return this.value;
    }
}

class Size {
    constructor(width, height) {
        this.width = 0;
        this.height = 0;
        this.width = width;
        this.height = height;
    }
    toJson() {
        return {
            width: this.width,
            height: this.height,
        };
    }
    toArray() {
        return [this.width, this.height];
    }
}

var AnimationState;
(function (AnimationState) {
    AnimationState[AnimationState["running"] = 0] = "running";
    AnimationState[AnimationState["stop"] = 1] = "stop";
})(AnimationState || (AnimationState = {}));
var CanvasSize = new Size(0, 0);
var RenderConfig = {
    grivity: .98,
};
class CanvasRender {
    constructor() {
        //画布大小
        this.canvasSize = new Size(300, 300);
        //动画状态
        this.animationState = AnimationState.stop;
        //动画完成回调
        this.onFinished = () => { };
        //图形
        this.shapeList = [];
        //渲染器是否被销毁
        this.hasBeenDispose = false;
        //回收栈
        this.revoveryShape = [];
        //FPS工具
        this.fpsUtil = new FpsUtil();
        //显示FPS
        this.displayFPS = false;
    }
    /**
     * @description 初始化渲染器时必须传入 画笔
     * @param paint
     * @param size
     * @param params
     */
    init(paint, size, option) {
        if (paint)
            this.paint = new Painter(paint);
        if (size) {
            this.canvasSize.width = size.width;
            this.canvasSize.height = size.height;
            CanvasSize = this.canvasSize;
        }
        if (option) {
            if (option.onFinished)
                this.onFinished = option.onFinished;
            if (option.displayFps)
                this.displayFPS = option.displayFps;
            RenderConfig.grivity = option.grivaty || .30;
        }
    }
    update(animationEngine) {
        if (this.animationState === AnimationState.stop)
            return;
        this.paint.clearRect(0, 0, this.canvasSize.width, this.canvasSize.height);
        if (this.shapeList.length == 0)
            return this.animationFinished();
        const hx = this.canvasSize.width >> 1, hy = this.canvasSize.height >> 1;
        this.shapeList.forEach((shape, ndx) => {
            if (!((shape.position.x < hx && shape.position.x > ~hx) && shape
                .position.y < hy)) {
                shape.disable();
            }
            shape.update(this.paint);
        });
        //回收对象
        this.recovery();
        //渲染FPS
        if (this.displayFPS) {
            const fps = this.fpsUtil.tick();
            this.paint.fillStyle = "black";
            this.paint.fillText("FPS:" + fps, 10, 20);
        }
        animationEngine(() => {
            this.update(animationEngine);
        });
    }
    animationFinished() {
        var _a;
        this.animationState = AnimationState.stop;
        (_a = this.onFinished) === null || _a === void 0 ? void 0 : _a.call(this);
    }
    /**
      * @description 回收彩纸对象
      */
    recovery() {
        this.shapeList = this.shapeList.filter((item, ndx) => {
            if (!item.alive) {
                this.revoveryShape.push(item);
            }
            return item.alive;
        });
    }
    /**
     * @description 在回收栈里面拿重复利用对象
     * @param {number} count //拿多少个
     */
    recover(count) {
        return __awaiter(this, void 0, void 0, function* () {
            if (this.hasBeenDispose) {
                throw new Error('This CanvasRender has been destroyed!');
            }
            const len = this.revoveryShape.length;
            if (count > len) {
                const re = [];
                for (let i = 0; i < len; i++) {
                    re.push(this.revoveryShape.pop());
                }
                return Promise.resolve(re);
            }
            else {
                const re = [];
                for (let i = 0; i < count; i++) {
                    re.push(this.revoveryShape.pop());
                }
                return Promise.resolve(re);
            }
        });
    }
    /**
     * @description 销毁渲染器，释放所有内存，无法再继续使用
     * @returns void
     */
    dispose() {
        if (this.hasBeenDispose)
            return;
        this.hasBeenDispose = true;
        this.animationState = AnimationState.stop;
        this.paint.clearRect(0, 0, this.canvasSize.width, this.canvasSize.height);
        this.paint = this.shapeList = this.revoveryShape = this.fpsUtil = null;
    }
    /**
     * @description 运行
     * @returns
     */
    run() {
        if (this.hasBeenDispose) {
            return console.error("CanvasRender has been destroyed!");
        }
        let animationEngine = function (callback) {
            setTimeout(callback, 1000 / 60);
        };
        if (typeof (requestIdleCallback) != "undefined") {
            animationEngine = requestAnimationFrame;
        }
        animationEngine(() => {
            if (this.shapeList.length != 0)
                this.update(animationEngine);
        });
    }
    add(shapes) {
        /*fire的时候继续开启动画状态*/
        if (this.animationState == AnimationState.stop) {
            this.animationState = AnimationState.running;
            this.run();
        }
        this.shapeList.push(...shapes);
    }
}

/*矩阵工具类*/
class Matrix3 {
    static transformTo2D(point, A, position) {
        return {
            x: (CanvasSize.width * .5) + position.x + point.x,
            y: (CanvasSize.height * .5) + position.y + point.y //+y,
        };
    }
    static rotateX(point, angle) {
        //const mp = point.toArray();
        const cos_ = Math.cos(angle), sin_ = Math.sin(angle);
        const y = point.y * cos_ - point.z * sin_;
        const z = point.z * cos_ + point.y * sin_;
        const result = [point.x, y, z];
        // const mf = [
        // 	[1, 0, 0],
        // 	[0, cos_, (sin_*-1)],
        // 	[0, sin_, cos_]
        // ];
        return result;
    }
    static rotateZ(point, angle) {
        //const mp = point.toArray();
        const cos_ = Math.cos(angle), sin_ = Math.sin(angle);
        const x = point.x * cos_ - point.y * sin_;
        const y = point.x * sin_ + point.y * cos_;
        return [x, y, point.z];
        // const mf = [
        // 	[cos_, (sin_*-1) , 0],
        // 	[sin_, cos_, 0],
        // 	[0, 0, 1]
        // ];
    }
    static rotateY(point, angle) {
        const cos_ = Math.cos(angle), sin_ = Math.sin(angle);
        const x = point.x * cos_ - point.z * sin_;
        const z = point.z * cos_ + point.x * sin_;
        return [x, point.y, z];
    }
}
class Matrix3All {
    static rotateX(shape, angle) {
        shape.points.forEach((item) => {
            const newPoint = Matrix3.rotateX(item, Matrix3All.PI * angle);
            item.setPoint(newPoint);
        });
    }
    static rotateY(shape, angle) {
        shape.points.forEach((item) => {
            const newPoint = Matrix3.rotateY(item, Matrix3All.PI * angle);
            item.setPoint(newPoint);
        });
    }
    static rotateZ(shape, angle) {
        shape.points.forEach((item) => {
            const newPoint = Matrix3.rotateZ(item, Matrix3All.PI * angle);
            item.setPoint(newPoint);
        });
    }
}
Matrix3All.PI = Math.PI / 180;

class Vector {
    constructor(x, y) {
        this.x = 0;
        this.y = 0;
        this.x = x;
        this.y = y;
    }
    add(v) {
        this.x += v.x;
        this.y += v.y;
    }
    sub(v) {
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }
    mult(v) {
        this.x *= v.x;
        this.y *= v.y;
        return this;
    }
    div(v) {
        this.x /= v.x;
        this.y /= v.y;
        return this;
    }
    mag() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }
    dist(v) {
        let dx = this.x - v.x;
        let dy = this.y - v.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
    normalize() {
        let len = this.mag();
        this.x /= len;
        this.y /= len;
        return this;
    }
    clamp(c) {
        let [max, min] = c;
        this.x = Math.min(Math.max(this.x, min), max);
        this.y = Math.min(Math.max(this.y, min), max);
    }
    copy() {
        return new Vector(this.x, this.y);
    }
    set(v) {
        this.x = v.x;
        this.y = v.y;
    }
    setXY(x, y) {
        this.x = x;
        this.y = y;
    }
    toJson() {
        return {
            x: this.x,
            y: this.y,
        };
    }
    toArray() {
        return [this.x, this.y];
    }
    static dist(v1, v2) {
        let sub = Vector.sub(v1, v2);
        return Vector.mag(sub);
    }
    static mag(v) {
        return Math.sqrt(v.x * v.x + v.y * v.y);
    }
    static sub(v1, v2) {
        return new Vector(v1.x - v2.x, v1.y - v2.y);
    }
}

class Point extends Vector {
    constructor(x, y) {
        super(x, y);
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.x = x;
        this.y = y;
    }
    setPoint(point) {
        const [x, y, z] = point;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    toArray() {
        return [this.x, this.y];
    }
}
/**
 * 纸屑类基类
 */
class Shape {
    constructor() {
        this.points = [];
        this.position = new Vector(0, 0);
        this.vector = new Vector(0, 0);
        this._alive = true;
        this.gravity = RenderConfig.grivity;
    }
    get alive() {
        return this._alive;
    }
    relive() {
        this._alive = true;
    }
    disable() {
        this._alive = false;
    }
    update(paint) {
        this.move();
        this.material.update(paint, this.position, this);
        const speed = 20;
        const ran = () => Math.random() * speed;
        Matrix3All.rotateX(this, ran() - this.vector.y);
        Matrix3All.rotateY(this, ran() - this.vector.x);
        Matrix3All.rotateZ(this, ran() - this.vector.y);
    }
    move() {
        if (Math.abs(this.vector.x) > .2)
            this.vector.x *= .9;
        if (Math.abs(this.vector.y) > 1)
            this.vector.y *= .9;
        this.vector.y += this.gravity;
        this.vector.x += Math.random() > .5 ? -.2 : .2;
        this.position.add(this.vector);
    }
    reset(reset) {
        this.relive();
        this.material.opacity = 1;
        this.position.setXY(reset.position.x, reset.position.y);
        this.vector.setXY(reset.vector.x, reset.vector.y);
    }
}

class Material {
    constructor(points) {
        this.A = new Point(0, 0);
        this.points = [];
        this.opacity = 1;
        this.points = points;
        const colorAndKey = Styles.RandomColor;
        this._color = colorAndKey.color;
        this._color_key = colorAndKey.key;
    }
    update(paint, position, shape) {
        if (this.opacity <= .05) {
            return shape.disable();
        }
        this.opacity -= .004;
        this.draw(paint, position);
    }
    draw(paint, position) {
        //paint.fillRect(position.x,position.y,10,10)
        paint.beginPath();
        //if(!this._color)return;
        this._color[3] = this.opacity;
        /*判断上次颜色是否和这次一样*/
        paint.fillStyle = Styles.rgba(this._color);
        this.points.forEach((point) => {
            const dp = Matrix3.transformTo2D(point, this.A, position);
            paint.lineTo(dp.x, dp.y);
        });
        paint.closePath();
        paint.fill();
    }
}
class Styles {
    /**
     * @param {Array<String|Array>} colors
     * @description 自定义颜色转换
     */
    static setColors(colors) {
        if (colors.length == 0)
            return;
        Styles._colors = [];
        colors.forEach((item) => {
            if (item instanceof Array) {
                Styles._colors.push(item);
            }
            else if (typeof (item) === 'string') {
                Styles._colors.push(ColorUtil.transformHexToRGB(item));
            }
        });
    }
    static get RandomColor() {
        const colors = Styles._colors;
        const ran = (Math.random() * colors.length) >> 0;
        const color = colors[ran];
        return {
            key: ran,
            color: [...color],
        };
    }
    static rgba(color) {
        const [r, g, b, a] = color;
        return `rgba(${r},${g},${b},${a})`;
    }
}
Styles._colors = [
    [253, 101, 255],
    [163, 253, 130],
    [183, 128, 253],
    [89, 214, 255],
    [253, 186, 96],
    [251, 253, 113],
];
class ColorUtil {
    /**
     * @param {Sting} hex
     * @description 将十六进制转换成rgb数组形式
     */
    static transformHexToRGB(hex) {
        const len = hex.length;
        let newHex = hex;
        /*不足六位补齐*/
        if (len < 6) {
            for (let i = 0; i < 6 - len; i++) {
                newHex += "0";
            }
        }
        const rgbArr = [];
        for (let i = 0; i <= 2; i++) {
            const hex_2 = newHex.substr(i * 2, 2);
            rgbArr[i] = parseInt(hex_2, 16);
        }
        return rgbArr;
    }
}

class CustomShape extends Shape {
    constructor(params) {
        super();
        this._originPoints = [];
        this.scale = 1;
        this._originPoints = params.points;
        this.scale = params.scale || 1;
        this.createPosints(params.points);
        this.material = new Material(this.points);
        if (params.position)
            this.position = params.position;
        if (params.vector)
            this.vector = params.vector;
    }
    get originPoints() {
        return this._originPoints;
    }
    createPosints(points = []) {
        const len = points.length;
        for (let i = 0; i < len; i++) {
            let [x, y] = points[i];
            if (this.scale != 1) {
                x *= this.scale;
                y *= this.scale;
            }
            this.points.push(new Point(x, y));
        }
    }
}

class Polygon extends Shape {
    constructor(params) {
        super();
        this.size = params.size;
        this.createPosints(params.count);
        this.material = new Material(this.points);
        this.vector = params.vector;
        this.position = params.position;
    }
    createPosints(count) {
        const PI = Math.PI * 2;
        const half_w = this.size.width >> 1;
        const avg = PI / count;
        for (let i = 0; i < count; i++) {
            const point = new Point(Math.cos(i * avg) * half_w, Math.sin(i * avg) * half_w);
            this.points.push(point);
        }
    }
}

class Utils {
    static constructorIs(obj, constructorName) {
        if ((typeof obj) == 'object') {
            return obj.constructor.name === constructorName;
        }
        return false;
    }
}

/*形状类型枚举*/
class ConfettiShapes {
}
ConfettiShapes.POLYGON = 'Polygon';
ConfettiShapes.CUSTOM = 'CustomShape';
/*喷发类*/
class ConfettiEjector {
    constructor(canvasRender, params) {
        /*彩纸片边角数量集合*/
        this.shapeTypes = [3, 4, 15];
        this.PI = Math.PI / 180;
        /*每次爆炸彩纸数量*/
        this.count = 30;
        //限制喷射角度，顺时针
        this.limitAngle = [90, 270];
        const { limitAngle, count, shapeTypes, colors } = params || {};
        if (canvasRender == undefined) {
            console.error("CanvasRender不能为空");
            return;
        }
        this.canvasRender = canvasRender;
        this.limitAngle = limitAngle || [0, 360];
        this.count = count || 30;
        this.shapeTypes = shapeTypes || [3, 4, 5, 6, 15];
        if (colors) {
            Styles.setColors(colors);
        }
    }
    /*获取指定区间值*/
    getRandomClamp([min, max]) {
        const ran = Math.random() * (max - min + 1) + min;
        return ran;
    }
    create(params) {
        return __awaiter(this, void 0, void 0, function* () {
            const { x, y, clampforce, radius } = params;
            //喷射速度
            const spraySpeed = clampforce || [20, 40];
            const shapesCache = [];
            /*重新使用被回收的对象*/
            const recover = yield this.canvasRender.recover(this.count);
            const len = recover.length;
            for (let i = 0; i < len; i++) {
                const shape = recover[i];
                const ranAngle = this.getRandomClamp(this.limitAngle) * this.PI;
                const speed = this.getRandomClamp(spraySpeed);
                const vx = Math.cos(ranAngle) * speed;
                const vy = Math.sin(ranAngle) * speed;
                shape.reset({
                    position: new Vector(x, y),
                    vector: new Vector(vx, vy)
                });
            }
            shapesCache.push(...recover);
            for (let i = 0; i < this.count - len; i++) {
                const shapeType = this.shapeTypes[(Math.random() * this.shapeTypes.length) >> 0];
                const ranAngle = this.getRandomClamp(this.limitAngle) * this.PI;
                const speed = this.getRandomClamp(spraySpeed);
                const vx = Math.cos(ranAngle) * speed;
                const vy = Math.sin(ranAngle) * speed;
                if (Utils.constructorIs(shapeType, ConfettiShapes.CUSTOM)) {
                    const _ = shapeType;
                    const originCustomShape = _;
                    const customShape = new CustomShape({
                        points: originCustomShape.originPoints,
                        position: new Vector(x, y),
                        vector: new Vector(vx, vy),
                        scale: originCustomShape.scale,
                    });
                    shapesCache.push(customShape);
                    continue;
                }
                const _ = shapeType;
                const count = _;
                const shape = new Polygon({
                    size: new Size(radius, radius),
                    count: count,
                    position: new Vector(x, y),
                    vector: new Vector(vx, vy),
                });
                shapesCache.push(shape);
            }
            return Promise.resolve(shapesCache);
        });
    }
    fire(_shapes) {
        return __awaiter(this, void 0, void 0, function* () {
            const shapes = yield _shapes;
            this.canvasRender.add(shapes);
        });
    }
}

export { CanvasRender, CanvasSize, ConfettiEjector, CustomShape, Polygon };
