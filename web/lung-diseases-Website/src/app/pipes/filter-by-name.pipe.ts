import { Pipe, PipeTransform } from '@angular/core';
import { User } from '../models/User.model';

@Pipe({
  name: 'filterByName'
})
export class FilterByNamePipe implements PipeTransform {

  transform(itemList: User[], searchKeyword: string) {

    if (!itemList) {
      return [];
    }

    if (!searchKeyword) {
      return itemList;
    }

    let filteredList = [];

    if (itemList.length > 0) {
      // searchKeyword = searchKeyword.toLowerCase();

      itemList.forEach(item => {
        let propValueList = Object.values(item);
        for (let i = 0; i < propValueList.length; i++) {
          if (propValueList[i]) {
            if (propValueList[i].toString().toLowerCase().indexOf(searchKeyword) > -1) {
              filteredList.push(item);
              break;
            } else if (item.firstName.includes(searchKeyword) ||
                      item.lastName.includes(searchKeyword)) {
              filteredList.push(item);
              break;
            }
          }
        }
      });
    }
    return filteredList;
  }
}
