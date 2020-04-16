export class TenserFlow {
    id: number;
    diagnosis: string;
    confidence: number;
    nodule: {
      id: number,
      x: number,
      y: number,
      z: number
    };
}
