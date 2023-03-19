declare class Vector {
    x: number;
    y: number;
    constructor(x: number, y: number);
    add(v: Vector): void;
    sub(v: Vector): this;
    mult(v: Vector): this;
    div(v: Vector): this;
    mag(): number;
    dist(v: Vector): number;
    normalize(): this;
    clamp(c: [max: number, min: number]): void;
    copy(): Vector;
    set(v: Vector): void;
    setXY(x: number, y: number): void;
    toJson(): {
        x: number;
        y: number;
    };
    toArray(): number[];
    static dist(v1: Vector, v2: Vector): number;
    static mag(v: Vector): number;
    static sub(v1: Vector, v2: Vector): Vector;
}
export default Vector;
//# sourceMappingURL=vector.d.ts.map