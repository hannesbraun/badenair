import {Component, OnInit} from '@angular/core';

import {timer} from 'rxjs';

import {
    getViewBoxConfig,
} from '../../services/util/SVGUtil';

@Component({
    selector: 'app-gol',
    templateUrl: './gol.component.html',
    styleUrls: ['./gol.component.scss']
})

export class GolComponent implements OnInit {
    gameBoard: boolean[][] = [];

    readonly boardWidth: number = 50;
    readonly boardHeight: number = 50;

    drawView: boolean = true;
    running: boolean = false;

    constructor() {
    }

    ngOnInit() {
        // TODO remove subscription to Service

        for (let i = 0; i < this.boardWidth; i++){
            let temp: boolean[] = [];
            for (let j = 0; j < this.boardHeight; j++){
                temp.push(false);
            }
            this.gameBoard.push(temp);
        }

        const refreshTimer = timer(0, 200);
        refreshTimer.subscribe(this.simulateLifeCycle);
    }

    simulateLifeCycle = () => {
        if (this.running == true){
            const newBoard: boolean[][] = [];
            for (let i = 0; i < this.boardWidth; i++){
                newBoard[i] = [];
                for (let j = 0; j < this.boardHeight; j++){
                    let nNeighbours = this.getAliveNeighbours(i, j);

                    newBoard[i][j] = this.gameBoard[i][j];

                    if (nNeighbours < 2)
                        newBoard[i][j] = false;
    
                    if (nNeighbours > 3)
                        newBoard[i][j] = false;
    
                    if (nNeighbours == 3)
                        newBoard[i][j] = true;
                }
            }

            this.gameBoard = newBoard;
        }
    }

    getAliveNeighbours(x: number, y: number): number{
        let xDiff: number[] = [-1, 0, 1, -1, 1, -1, 0, 1];
        let yDiff: number[] = [-1, -1, -1, 0, 0, 1, 1, 1];
        let ret = 0;

        for (let i = 0; i < 8; i++){
            if (x + xDiff[i] >= 0 && x + xDiff[i] < this.boardWidth){
                if (y + yDiff[i] > 0 && y + yDiff[i] < this.boardHeight){
                    if (this.gameBoard[x + xDiff[i]][y + yDiff[i]] == true)
                        ret++;
                }
            }
        }

        return ret;
    }

    toggleLifeState(x: number, y: number){
        if (this.running == false)
            this.gameBoard[x][y] = !this.gameBoard[x][y];
    }

    toggleRunning(){
        this.running = !this.running;
    }

    get viewBoxConfig(): string {
        return getViewBoxConfig(500);
    }
}