export interface Need {
    id: number;
    name: string;
    value: number;
    currentQuantity: number;
    maxQuantity: number;
    type: string;
    visible: boolean;
}