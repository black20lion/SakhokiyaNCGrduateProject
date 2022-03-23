import {Component, Input, OnInit} from '@angular/core';
import {Icon} from "../main-page/main-page.component";

@Component({
  selector: 'app-icon',
  templateUrl: './icon.component.html',
  styleUrls: ['./icon.component.scss']
})
export class IconComponent implements OnInit {

  @Input() icon!: Icon
  @Input() index!: number

  opacity: number = 0.5

  hrefs: string[] =["#zatemnenie", "#zatemnenie", "#zatemnenie"]

  ngOnInit(): void {
  }
}
